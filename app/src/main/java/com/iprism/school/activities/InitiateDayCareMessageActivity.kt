package com.iprism.school.activities

import android.annotation.SuppressLint
import android.content.Intent
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.adapters.DayCareMessageStudentsAdapter
import com.iprism.school.adapters.StudentMessageSelectAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityCreateDayCareAlbumsBinding
import com.iprism.school.databinding.ActivityInitiateDayCareMessageBinding
import com.iprism.school.databinding.DialogSelectStudentsBinding
import com.iprism.school.databinding.FileTypeBottomSheetBinding
import com.iprism.school.interfaces.OnMessageClickListener
import com.iprism.school.model.classteachermodel.Class
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.Section
import com.iprism.school.model.daycare.Category
import com.iprism.school.model.daycare.DayCareApiRequest
import com.iprism.school.model.messagemodel.DayCareMessagesApiRequest
import com.iprism.school.model.messagemodel.MessagesApiRequest
import com.iprism.school.model.studentsmodel.Student
import com.iprism.school.model.studentsmodel.StudentsApiRequest
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.DayCareMessagesRepository
import com.iprism.school.repositories.DayCareRepository
import com.iprism.school.repositories.MessagesRepository
import com.iprism.school.repositories.StudentsRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.DayCareAttendanceViewModel
import com.iprism.school.viewModels.DayCareMessagesViewModel
import com.iprism.school.viewModels.DayCareViewModel
import com.iprism.school.viewModels.MessagesViewModel
import com.iprism.school.viewModels.StudentsViewModel
import com.iprism.school.viewModels.ViewModelFactory

class InitiateDayCareMessageActivity : BaseActivity() {

    private lateinit var binding: ActivityInitiateDayCareMessageBinding

    private lateinit var daycareViewModel: DayCareViewModel
    private lateinit var messageViewModel: DayCareMessagesViewModel
    private var planId: String = "-1"
    private lateinit var fileTypeBinding: FileTypeBottomSheetBinding
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var pickFileLauncher: ActivityResultLauncher<Array<String>>
    private var selectedFileUri: Uri? = null
    private lateinit var studentsBottomSheetBinding: DialogSelectStudentsBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var studentsList = mutableListOf<com.iprism.school.model.daycare.Student>()
    private lateinit var studentsAdapter: DayCareMessageStudentsAdapter
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
        binding = ActivityInitiateDayCareMessageBinding.inflate(layoutInflater)
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

                    val base64 = convertUriToBase64(it)
                    Log.d("BASE64_IMAGE", base64)
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

                    val base64 = convertUriToBase64(it)
                    Log.d("BASE64_FILE", base64)
                }
            }
        initViewModel()
        observePlansResponse()
        observeStudentsResponse()
        observeSendMessageResponse()
        handleSentBtn()
        handleBack()
        handleCrossBtn()
        handleSelectStudentsLo()
        handleAttachmentBtn()
        var request = DayCareApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            "",
            "",
            userDetails[User.SCHOOL_ID].toString(),
            "",
            "",
            "",
            1,
            "",
            "",
            userDetails[User.ID].toString(),
            "categories",
            ""
        )
        daycareViewModel.fetchDayCarePlans(request)
    }

    private fun loadStudents() {
        val request = DayCareApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            "",
            "",
            userDetails[User.SCHOOL_ID].toString(),
            planId,
            "",
            "",
            currentPage,
            "",
            "",
            userDetails[User.ID].toString(),
            "students",
            ""
        )

        Log.d("StudentsApiRequest", request.toString())
        daycareViewModel.fetchDayCareStudents(request)
    }

    private fun loadMoreItems() {
        if (isLastPage || isLoading) return
        isLoading = true
        currentPage++
        loadStudents()
    }

    private fun observePlansResponse() {
        daycareViewModel.dayCarePlansResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.categories.isNotEmpty()) {
                        var updatedList = result.data.categories.toMutableList()
                        updatedList.add(0, Category("-1", "Select Plan"))
                        setupPlansAdapter(updatedList)
                    } else {
                        ToastUtils.showErrorCustomToast(this, "No Daycare Plans Found..!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun setupPlansAdapter(plans: List<Category>) {
        var namesList = plans.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.plansSp.adapter = adapter
        binding.plansSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    planId = plans[position].cat_id.toString()
                    value = ""
                    studentId = "-1"
                    studentName = ""
                    binding.selectStudentTxt.text = "Select Student"
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun observeStudentsResponse() {
        daycareViewModel.dayCareStudentsResponse.observe(this) { result ->

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

    private fun setupRecyclerView() {
        studentsAdapter = DayCareMessageStudentsAdapter(studentsList)
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
                    type: String,
                    studentId: String
                ) {

                }

                override fun onStudentSelectClick(
                    value: String,
                    studentId: String,
                    studentName: String
                ) {
                    this@InitiateDayCareMessageActivity.studentId = studentId
                    this@InitiateDayCareMessageActivity.studentName = studentName
                    this@InitiateDayCareMessageActivity.value = value
                    //  Log.d("SingleStudentDetails", studentName + ", " + value)
                }

                override fun onInnerItemClick(eventImage: String) {

                }

            })

        }

    }

    private fun handleSelectStudentsLo() {
        binding.selectStudentsLo.setOnClickListener { view ->
            if (planId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(this, "Please Select Daycare Category..!")
            }  else {
                openStudentsBottomSheets(planId)
            }
        }
    }

    private fun openStudentsBottomSheets(planId: String) {
        this.planId = planId

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
            if (studentId.isEmpty() || studentId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(this, "Please Select Student..!")
            } else {
                binding.selectStudentTxt.text = studentName
                Log.d("StudentDetails", studentId + ", " + studentName + ", " + value)
                bottomSheetDialog.dismiss()
            }

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
        studentsAdapter = DayCareMessageStudentsAdapter(studentsList)
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
            if (planId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(this, "Please Select Daycare Category..!")
            } else if (studentId.isEmpty() || studentId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(this, "Please Select Students..!")
            } else if (getMessage().isEmpty() && selectedFileUri == null) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Message or Select File..!")
            } else {
                val request = DayCareMessagesApiRequest(
                    userDetails[User.SCHOOL_ID].toString(),
                    planId,
                    convertUriToBase64(selectedFileUri),
                    getMessage(),
                    value,
                    "1",
                    "teacher",
                    studentId,
                    "",
                    userDetails[User.ID].toString(),
                    "insert"
                )
                messageViewModel.insertMessage(request)
                Log.d("RequestInsert", request.toString())
            }
        }
    }

    private fun initViewModel() {
        val repository = DayCareMessagesRepository(this)
        messageViewModel = ViewModelProvider(
            this,
            ViewModelFactory { DayCareMessagesViewModel(repository) })[DayCareMessagesViewModel::class.java]

        val daycareRepository = DayCareRepository(this)
        val attendanceFactory = ViewModelFactory { DayCareViewModel(daycareRepository) }
        daycareViewModel =
            ViewModelProvider(this, attendanceFactory)[DayCareViewModel::class.java]

    }

    private fun observeSendMessageResponse() {
        messageViewModel.insertMessageResponse.observe(this) { result ->
            when (result) {

                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.sendBtn.isEnabled = false
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    var intent = Intent(this, SuccessActivity::class.java)
                    intent.putExtra("tag", "Message Sent")
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

    private fun convertUriToBase64(uri: Uri?): String {
        if (uri == null) return ""

        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            if (bytes != null) {
                Base64.encodeToString(bytes, Base64.NO_WRAP)
            } else {
                ""
            }

        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

}