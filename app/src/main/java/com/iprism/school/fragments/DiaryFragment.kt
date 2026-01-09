package com.iprism.school.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.school.R
import com.iprism.school.base.BaseFragment
import com.iprism.school.adapters.DiaryStudentsAdapter
import com.iprism.school.databinding.FragmentDiaryBinding
import com.iprism.school.model.classteachermodel.Class
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.Section
import com.iprism.school.model.dairy.DiaryApiRequest
import com.iprism.school.model.studentsmodel.Student
import com.iprism.school.model.studentsmodel.StudentsApiRequest
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.DiaryRepository
import com.iprism.school.repositories.StudentsRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.Utility
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.DiaryViewModel
import com.iprism.school.viewModels.StudentsViewModel
import com.iprism.school.viewModels.ViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DiaryFragment : BaseFragment() {

    private lateinit var binding: FragmentDiaryBinding
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var studentsViewModel: StudentsViewModel
    private lateinit var diariesViewModel: DiaryViewModel
    private var studentsList = mutableListOf<Student>()
    private lateinit var studentsAdapter: DiaryStudentsAdapter
    private var isFreshLoad = false
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10
    private var classId: String = "-1"
    private var sectionId: String = "-1"
    private var studentType: String = ""
    private var currentDate: String = ""
    private var diaryType: String = ""
    private var selectedImageUri: Uri? = null
    private var backendDate: String = ""
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var resultLaunchergallery: ActivityResultLauncher<Intent>

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryBinding.inflate(inflater, container, false)
        val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")
        currentDate = LocalDate.now().format(formatter)
        val formatterBackend = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        backendDate = LocalDate.now().format(formatterBackend)
        binding.dateTxt.text = currentDate
        binding.diaryTypeRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.class_work_rb -> {
                    diaryType = "cw"
                }

                R.id.home_work_rb -> {
                    diaryType = "hw"
                }
            }
        }

        binding.selectAllCb.setOnCheckedChangeListener { _, isChecked ->
            studentsAdapter.selectAll(isChecked)

            if (isChecked) {
                studentType = "all"
                binding.detailsLl.visibility = View.VISIBLE
            } else {
                studentType = "single"
                binding.detailsLl.visibility = View.GONE
            }
        }

        initViewModel()
        observeClassesResponse()
        observeSectionsResponse()
        setupRecyclerView()
        observeStudentsResponse()
        handleRefreshLo()
        handleAllImagesLo()
        handleSaveBtn()
        observeInsertDiaryResponse()
        var requestClasses = ClassTeacherApiRequest(
            "",
            userDetails[User.ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            "classes"
        )
        attendanceViewModel.fetchClasses(requestClasses)
        return binding.root
    }

    private fun observeInsertDiaryResponse() {
        diariesViewModel.insertDiaryResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.saveDiaryBtn.isEnabled = false
                }

                is UiState.Success -> {
                    ToastUtils.showSuccessCustomToast(
                        requireContext(),
                        "Diary Inserted Successfully..!"
                    )
                    refreshItems()
                    binding.saveDiaryBtn.isEnabled = true
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(requireContext(), result.message)
                    binding.progress.hideProgress()
                    binding.saveDiaryBtn.isEnabled = true

                }
            }
        }
    }

    private fun handleSaveBtn() {
        binding.saveDiaryBtn.setOnClickListener { view ->
            if (classId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(requireContext(), "Please Select Class..!")
            } else if (sectionId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(requireContext(), "Please Select Section..!")
            } else if (diaryType.isEmpty()) {
                ToastUtils.showErrorCustomToast(
                    requireContext(),
                    "Please Select Class Work or Home Work..!"
                )
            } else if (studentType.isEmpty()) {
                ToastUtils.showErrorCustomToast(
                    requireContext(),
                    "Please Select Students to give Diary..!"
                )
            } else if (studentType.equals(
                    "all",
                    true
                ) && getDetails().isEmpty() && selectedImageUri == null
            ) {
                ToastUtils.showErrorCustomToast(
                    requireContext(),
                    "Please Enter Details or Select Image.."
                )
            } else {
                var request = DiaryApiRequest(
                    userDetails[User.ACADEMIC_YEAR_ID].toString(),
                    userDetails[User.SCHOOL_ID].toString(), classId, backendDate, getDetails(), "",
                    convertUriToBase64Image(selectedImageUri), currentPage, sectionId, studentType,
                    "", diaryType, userDetails[User.ID].toString(), "insert"
                )
                diariesViewModel.insertDiary(request)
            }
        }
    }

    private fun convertUriToBase64Image(imageUri: Uri?): String {
        if (imageUri == null) return ""

        return try {
            val inputStream = requireContext().contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (bitmap != null) {
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    byteArrayOutputStream
                ) // Use PNG if you prefer lossless
                val imageBytes = byteArrayOutputStream.toByteArray()
                Base64.encodeToString(imageBytes, Base64.DEFAULT)
            } else {
                ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    private fun getDetails(): String {
        return binding.etDetails.text.toString().trim()
    }

    private fun handleAllImagesLo() {
        binding.allPicImg.setOnClickListener { view ->
            selectImage()
        }
    }

    private fun loadStudents(isFromFilterChange: Boolean = false) {

        if (isLoading) return

        if (isFromFilterChange) {
            currentPage = 1
            isLastPage = false
            isFreshLoad = true

            studentsList.clear()
            studentsAdapter.notifyDataSetChanged()

            binding.studentsRv.visibility = View.GONE
            binding.noDataFoundTxt.visibility = View.VISIBLE
        }

        isLoading = true
        resetStudents()

        val request = StudentsApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            classId,
            currentPage,
            sectionId,
            userDetails[User.ID].toString()
        )

        Log.d("StudentsApiRequest", request.toString())
        studentsViewModel.fetchActiveStudents(request)
    }

    private fun refreshItems() {
        currentPage = 1
        isLastPage = false
        isLoading = false
        studentsList.clear()
        studentsAdapter.notifyDataSetChanged()
        loadStudents(isFromFilterChange = true)
    }

    private fun loadMoreItems() {
        if (isLastPage || isLoading) return
        isLoading = true
        currentPage++
        loadStudents()
    }

    private fun resetStudents() {
        currentPage = 1
        isLastPage = false
        isLoading = false

        studentsList.clear()
        studentsAdapter.notifyDataSetChanged()

        binding.studentsRv.visibility = View.GONE
        binding.noDataFoundTxt.visibility = View.VISIBLE
    }

    private fun handleRefreshLo() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                refreshItems()
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun setupRecyclerView() {
        studentsAdapter = DiaryStudentsAdapter(requireContext(), studentsList)
        val linearLayoutManager = LinearLayoutManager(requireContext())

        binding.studentsRv.apply {
            layoutManager = linearLayoutManager
            adapter = studentsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled =
                        !binding.studentsRv.canScrollVertically(-1)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage && studentsList.isNotEmpty()) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            loadMoreItems()
                        }
                    }

                }
            })
        }

    }

    private fun observeStudentsResponse() {
        studentsViewModel.activeStudentsResponse.observe(viewLifecycleOwner) { result ->

            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newEvents = result.data.students

                    if (newEvents.isNotEmpty()) {

                        if (isFreshLoad) {
                            studentsList.clear()
                            isFreshLoad = false
                        }

                        studentsList.addAll(newEvents)
                        studentsAdapter.notifyDataSetChanged()

                        binding.studentsRv.visibility = View.VISIBLE
                        binding.noDataFoundTxt.visibility = View.GONE

                        isLastPage = newEvents.size < limit
                    } else {
                        isLastPage = true

                        if (currentPage == 1) {
                            studentsList.clear()
                            studentsAdapter.notifyDataSetChanged()
                            binding.studentsRv.visibility = View.GONE
                            binding.noDataFoundTxt.visibility = View.VISIBLE
                        }
                    }

                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    if (studentsList.isEmpty()) {
                        binding.studentsRv.visibility = View.GONE
                        binding.noDataFoundTxt.visibility = View.VISIBLE
                        ToastUtils.showErrorCustomToast(requireContext(), result.message)
                    } else {
                        binding.studentsRv.visibility = View.VISIBLE
                        binding.noDataFoundTxt.visibility = View.GONE
                        ToastUtils.showErrorCustomToast(requireContext(), "There is no more data")
                    }
                }
            }
        }
    }

    private fun observeClassesResponse() {
        attendanceViewModel.classesResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.classes.isNotEmpty()) {
                        var updatedList = result.data.classes.toMutableList()
                        updatedList.add(0, Class("-1", "Select Class"))
                        setupClassesAdapter(updatedList)
                    } else {
                        ToastUtils.showErrorCustomToast(requireContext(), "No Classes Found..!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(requireContext(), result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun observeSectionsResponse() {
        attendanceViewModel.sectionsResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.sections.isNotEmpty()) {
                        var updatedList = result.data.sections.toMutableList()
                        updatedList.add(0, Section("-1", "Select Section"))
                        setupSectionsAdapter(updatedList)
                    } else {
                        ToastUtils.showErrorCustomToast(requireContext(), "No Classes Found..!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(requireContext(), result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun setupClassesAdapter(genderTypes: List<Class>) {
        var namesList = genderTypes.map { it.class_name }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, namesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.classesSp.adapter = adapter
        binding.classesSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    classId = genderTypes[position].class_id.toString()
                    if (!classId.equals("-1", true)) {
                        var requestClasses = ClassTeacherApiRequest(
                            classId,
                            userDetails[User.ID].toString(),
                            userDetails[User.SCHOOL_ID].toString(),
                            userDetails[User.ACADEMIC_YEAR_ID].toString(),
                            "sections"
                        )
                        attendanceViewModel.fetchSections(requestClasses)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun setupSectionsAdapter(genderTypes: List<Section>) {
        var namesList = genderTypes.map { it.section_name }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, namesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sectionsSp.adapter = adapter
        binding.sectionsSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sectionId = genderTypes[position].section_id.toString()
                    if (!sectionId.equals("-1", true)) {
                        loadStudents()
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun initViewModel() {
        val repository = AttendanceRepository(requireContext())
        val factory = ViewModelFactory { AttendanceViewModel(repository) }
        attendanceViewModel = ViewModelProvider(this, factory)[AttendanceViewModel::class.java]

        val studentsRepository = StudentsRepository(requireContext())
        val studentsFactory = ViewModelFactory { StudentsViewModel(studentsRepository) }
        studentsViewModel = ViewModelProvider(this, studentsFactory)[StudentsViewModel::class.java]

        val diaryRepository = DiaryRepository(requireContext())
        val diaryFactory = ViewModelFactory { DiaryViewModel(diaryRepository) }
        diariesViewModel = ViewModelProvider(this, diaryFactory)[DiaryViewModel::class.java]
    }

    private fun selectImage() {
        val items = arrayOf("Take Photo", "Choose from Gallery", "Cancel")

        AlertDialog.Builder(
            requireContext(),
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
        )
            .setTitle("Add Photo!")
            .setItems(items) { dialog, which ->
                when (items[which]) {
                    "Take Photo" -> openCamera()
                    "Choose from Gallery" -> openGallery()
                    "Cancel" -> dialog.dismiss()
                }
            }
            .show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun setFileName(uri: Uri?) {
        uri ?: return
        var fileName = ""

        requireContext().contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst()) {
                fileName = cursor.getString(nameIndex)
            }
        }

        binding.fileNameTxt.text = fileName
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                selectedImageUri = getImageUriFromBitmap(bitmap)
                setFileName(selectedImageUri)
            }
        }


    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                setFileName(it)
            }
        }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val path = MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            bitmap,
            "IMG_${System.currentTimeMillis()}",
            null
        )

        return Uri.parse(path)
    }

}