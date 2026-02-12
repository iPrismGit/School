package com.iprism.school.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.activities.album.CreateDayCareAlbumsActivity
import com.iprism.school.adapters.AttandanceStudentsAdapter
import com.iprism.school.adapters.DiaryStudentsAdapter
import com.iprism.school.adapters.MessagesAdapter
import com.iprism.school.adapters.StudentMessageSelectAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityInitiateMessageBinding
import com.iprism.school.databinding.DialogSelectStudentsBinding
import com.iprism.school.databinding.FileTypeBottomSheetBinding
import com.iprism.school.interfaces.OnAttendanceClickListener
import com.iprism.school.interfaces.OnMessageClickListener
import com.iprism.school.model.classteachermodel.AttendanceStudent
import com.iprism.school.model.classteachermodel.AttendanceStudentsApiRequest
import com.iprism.school.model.classteachermodel.Class
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.Section
import com.iprism.school.model.messagemodel.MessageThread
import com.iprism.school.model.messagemodel.MessagesApiRequest
import com.iprism.school.model.studentsmodel.Student
import com.iprism.school.model.studentsmodel.StudentsApiRequest
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.MessagesRepository
import com.iprism.school.repositories.StudentsRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.MessagesViewModel
import com.iprism.school.viewModels.StudentsViewModel
import com.iprism.school.viewModels.ViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.IOException

class InitiateMessageActivity : BaseActivity() {

    private lateinit var binding: ActivityInitiateMessageBinding
    private lateinit var viewModel: MessagesViewModel
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var studentsViewModel: StudentsViewModel
    private lateinit var fileTypeBinding: FileTypeBottomSheetBinding

    // private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var pickFileLauncher: ActivityResultLauncher<Array<String>>
    private var selectedFileUri: Uri? = null
    private var classId: String = "-1"
    private var sectionId: String = "-1"
    private val studentList = ArrayList<Student?>()
    private lateinit var studentsBottomSheetBinding: DialogSelectStudentsBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var studentsList = mutableListOf<Student>()
    private lateinit var studentsAdapter: StudentMessageSelectAdapter
    private var isFreshLoad = false
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10
    private var studentId = ""
    private var studentName = ""
    private var value = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInitiateMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                uri?.let {
                    handleSelectedFile(it)
                }
            }

        pickFileLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                uri?.let {
                    contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    handleSelectedFile(it)
                }
            }
        initViewModel()
        observeClassesResponse()
        observeSectionsResponse()
        observeSendMessageResponse()
        handleSentBtn()
        handleBack()
        handleCrossBtn()
        handleSelectStudentsLo()
        handleAttachmentBtn()
        var requestClasses = ClassTeacherApiRequest(
            "",
            userDetails[User.ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            "classes"
        )
        attendanceViewModel.fetchClasses(requestClasses)
        observeStudentsResponse()
    }

    private fun handleSelectStudentsLo() {
        binding.selectStudentsLo.setOnClickListener { view ->
            if (classId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(this, "Please Select Class..!")
            } else if (sectionId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(this, "Please Select Section..!")
            } else {
                openStudentsBottomSheets(classId, sectionId)
            }
        }
    }

    private fun openStudentsBottomSheets(classId: String, sectionId: String) {
        this.classId = classId
        this.sectionId = sectionId
        bottomSheetDialog = BottomSheetDialog(this)
        studentsBottomSheetBinding =
            DialogSelectStudentsBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(studentsBottomSheetBinding.root)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        initializeBottomSheet()
        studentsBottomSheetBinding.btnBack.setOnClickListener { view ->
            bottomSheetDialog.dismiss()
        }
        studentsBottomSheetBinding.btnSave.setOnClickListener { view ->
            binding.selectStudentTxt.text = studentName
            Log.d("StudentDetails", studentName + ", " + value)
            bottomSheetDialog.dismiss()
        }
        studentsBottomSheetBinding.tvSelectAll.setOnClickListener { view ->
            studentsAdapter.selectAllStudents()
        }
        bottomSheetDialog.show()
    }

    private fun initializeBottomSheet() {
        currentPage = 1
        isLastPage = false
        isLoading = false
        studentsList.clear()
        studentsAdapter = StudentMessageSelectAdapter(studentsList)
        setupRecyclerView()
        loadStudents()
    }

    private fun handleCrossBtn() {
        binding.removeIv.setOnClickListener {
            selectedFileUri = null
            binding.documentLo.visibility = View.GONE
            binding.btnAttachment.visibility = View.VISIBLE
            binding.fileNameTxt.text = ""
        }

    }

    private fun handleBack() {
        binding.ivBack.setOnClickListener { view ->
            finish()
        }
    }

    private fun handleSentBtn() {
        binding.sendBtn.setOnClickListener { view ->
            if (classId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(this, "Please Select Class..!")
            } else if (sectionId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(this, "Please Select Section..!")
            } else if (studentId.isEmpty() || studentId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(this, "Please Select Students..!")
            } else if (getMessage().isEmpty() || selectedFileUri == null) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Message or Select File..!")
            } else {
                var request = MessagesApiRequest(
                    userDetails[User.ACADEMIC_YEAR_ID].toString(),
                    userDetails[User.SCHOOL_ID].toString(),
                    classId,
                    convertUriToBase64Image(selectedFileUri),
                    getMessage(),
                    value,
                    1,
                    sectionId,
                    "teacher",
                    studentId,
                    "",
                    userDetails[User.ID].toString(),
                    "insert"
                )
                viewModel.insertMessage(request)
                Log.d("RequestInsert", request.toString())
            }
        }
    }

    private fun initViewModel() {
        val repository = MessagesRepository(this)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory { MessagesViewModel(repository) })[MessagesViewModel::class.java]

        val attendanceRepository = AttendanceRepository(this)
        val attendanceFactory = ViewModelFactory { AttendanceViewModel(attendanceRepository) }
        attendanceViewModel =
            ViewModelProvider(this, attendanceFactory)[AttendanceViewModel::class.java]

        val studentsRepository = StudentsRepository(this)
        val studentsFactory = ViewModelFactory { StudentsViewModel(studentsRepository) }
        studentsViewModel = ViewModelProvider(this, studentsFactory)[StudentsViewModel::class.java]

    }

    private fun observeSendMessageResponse() {
        viewModel.insertMessageResponse.observe(this) { result ->
            when (result) {

                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.sendBtn.isEnabled = false
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    var intent = Intent(this, SuccessActivity::class.java)
                    intent.putExtra("tag", "Message Sent ")
                    startActivity(intent)
                }

                is UiState.Error -> {
                    binding.sendBtn.isEnabled = true
                    binding.progress.hideProgress()
                    ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
    }

    private fun observeClassesResponse() {
        attendanceViewModel.classesResponse.observe(this) { result ->
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
                        ToastUtils.showErrorCustomToast(this, "No Classes Found..!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun observeSectionsResponse() {
        attendanceViewModel.sectionsResponse.observe(this) { result ->
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
                        ToastUtils.showErrorCustomToast(this, "No Classes Found..!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun setupClassesAdapter(genderTypes: List<Class>) {
        var namesList = genderTypes.map { it.class_name }
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
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
                        value = ""
                        studentId = "-1"
                        studentName = ""
                        binding.selectStudentTxt.text = "Select Student"
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun setupSectionsAdapter(genderTypes: List<Section>) {
        var namesList = genderTypes.map { it.section_name }
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
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
                    value = ""
                    studentId = "-1"
                    studentName = ""
                    binding.selectStudentTxt.text = "Select Student"

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun loadStudents() {
        var request = StudentsApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            userDetails[User.SCHOOL_ID].toString(), classId, currentPage, sectionId,
            userDetails[User.ID].toString()
        )
        studentsViewModel.fetchActiveStudents(request)
        Log.d("StudentsFetchRequest", request.toString())
    }

    private fun loadMoreItems() {
        if (isLastPage || isLoading) return
        isLoading = true
        currentPage++
        loadStudents()
    }


    private fun setupRecyclerView() {
        studentsAdapter = StudentMessageSelectAdapter(studentsList)
        val linearLayoutManager = LinearLayoutManager(this)

        studentsBottomSheetBinding.rvStudents.apply {
            layoutManager = linearLayoutManager
            adapter = studentsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
//                    binding.refreshLayout.isEnabled =
//                        !binding.studentAttendanceRv.canScrollVertically(-1)
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
            studentsAdapter.setupListener(object : OnMessageClickListener {


                override fun onItemClick(
                    threadId: String,
                    name: String,
                    image: String,
                    type: String
                ) {

                }

                override fun onStudentSelectClick(
                    value: String,
                    studentId: String,
                    studentName: String
                ) {
                    this@InitiateMessageActivity.studentId = studentId
                    this@InitiateMessageActivity.studentName = studentName
                    this@InitiateMessageActivity.value = value
                    //  Log.d("SingleStudentDetails", studentName + ", " + value)
                }

            })

        }

    }

    private fun observeStudentsResponse() {
        studentsViewModel.activeStudentsResponse.observe(this) { result ->

            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        studentsBottomSheetBinding.progress2.showProgress()
                    }
                }

                is UiState.Success -> {
                    studentsBottomSheetBinding.progress2.hideProgress()
                    isLoading = false
                    val newStudents = result.data.students

                    if (newStudents.isNotEmpty()) {

                        studentsList.addAll(newStudents)
                        studentsAdapter.notifyDataSetChanged()
                        isLastPage = newStudents.size < limit
                        studentsBottomSheetBinding.rvStudents.visibility = View.VISIBLE
                        studentsBottomSheetBinding.noDataFoundLo.visibility = View.GONE
                    } else {
                        isLastPage = true

                        if (currentPage == 1) {
                            studentsList.clear()
                            studentsAdapter.notifyDataSetChanged()
                            studentsBottomSheetBinding.rvStudents.visibility = View.GONE
                            studentsBottomSheetBinding.noDataFoundLo.visibility = View.VISIBLE
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    studentsBottomSheetBinding.progress2.hideProgress()
                    if (studentsList.isEmpty()) {
                        studentsBottomSheetBinding.rvStudents.visibility = View.GONE
                        studentsBottomSheetBinding.noDataFoundLo.visibility = View.VISIBLE
                        ToastUtils.showErrorCustomToast(this, result.message)
                    } else {
                        studentsBottomSheetBinding.tvSelectAll.visibility = View.VISIBLE
                        studentsBottomSheetBinding.noDataFoundLo.visibility = View.GONE
                        ToastUtils.showErrorCustomToast(this, "There is no more data")
                    }
                }
            }
        }
    }

    private fun handleAttachmentBtn() {
        binding.btnAttachment.setOnClickListener { view ->
            openFileSelectingOptions()
        }
    }

    private fun openFileSelectingOptions() {
        bottomSheetDialog = BottomSheetDialog(this)
        fileTypeBinding = FileTypeBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(fileTypeBinding.root)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        fileTypeBinding.crossImg.setOnClickListener { view ->
            bottomSheetDialog.dismiss()
        }

        fileTypeBinding.fileLo.setOnClickListener { view ->
            bottomSheetDialog.dismiss()
            pickFileLauncher.launch(arrayOf("*/*"))
        }

        fileTypeBinding.galleryLo.setOnClickListener { view ->
            bottomSheetDialog.dismiss()
            pickImageLauncher.launch("image/*")
        }

        bottomSheetDialog.show()
    }

    private fun getMessage(): String {
        return binding.messageTxt.text.toString().trim()
    }


    private fun handleSelectedFile(uri: Uri) {
        selectedFileUri = uri
        val fileName = getFileNameFromUri(uri)
        binding.fileNameTxt.text = fileName
        binding.documentLo.visibility = View.VISIBLE
        binding.btnAttachment.visibility = View.GONE
    }

    private fun getFileNameFromUri(uri: Uri): String {
        var name = "Unknown File"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    name = it.getString(index)
                }
            }
        }
        return name
    }

    private fun convertUriToBase64Image(imageUri: Uri?): String {
        if (imageUri == null) return ""

        return try {
            val inputStream = contentResolver.openInputStream(imageUri)
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
}