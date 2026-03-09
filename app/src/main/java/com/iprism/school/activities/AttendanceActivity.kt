package com.iprism.school.activities

import com.iprism.school.repositories.AttendanceRepository
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.adapters.AttandanceStudentsAdapter
import com.iprism.school.databinding.ActivityAttendanceBinding
import com.iprism.school.databinding.AllStudentsPresentBottomSheetBinding
import com.iprism.school.interfaces.OnAttendanceClickListener
import com.iprism.school.model.classteachermodel.AttendanceStudent
import com.iprism.school.model.classteachermodel.AttendanceStudentsApiRequest
import com.iprism.school.model.classteachermodel.Class
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.Section
import com.iprism.school.model.classteachermodel.Student
import com.iprism.school.model.daycare.DayCareAttendanceApiRequest
import com.iprism.school.model.daycare.SelectedStudent
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.ViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AttendanceActivity : BaseActivity() {

    private lateinit var binding: ActivityAttendanceBinding
    private lateinit var attendanceViewModel: AttendanceViewModel
    private var attendanceStatus: String = ""
    private var selectValue: String = "single"
    private var isSelectAllChecked = false
    private lateinit var studentsAdapter: AttandanceStudentsAdapter
    private var studentsList = mutableListOf<Student?>()
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var selectedStudents = mutableListOf<AttendanceStudent>()
    private var classId: String = "-1"
    private var sectionId: String = "-1"
    private var currentDate: String = ""
    private var backendDate: String = ""
    private var notification_parent: String? = "no"
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var markAttendanceBinding: AllStudentsPresentBottomSheetBinding
    private val selectAllListener: CompoundButton.OnCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->

            if (attendanceStatus.equals("attendance_not_given", true)) {

                isSelectAllChecked = isChecked   // ⭐ store state

                if (isChecked) {
                    studentsAdapter.selectAll()
                } else {
                    studentsAdapter.clearAll()
                }

            } else {
                binding.checkBoxAll.setOnCheckedChangeListener(null)
                binding.checkBoxAll.isChecked = false
                binding.checkBoxAll.setOnCheckedChangeListener(selectAllListener)

                if (classId.equals("-1", true) || sectionId.equals("-1", true)) {
                    ToastUtils.showErrorCustomToast(this, "Please Select Class And Section..!")
                } else {
                    ToastUtils.showErrorCustomToast(this, "Attendance Already Given..!")
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")
        currentDate = LocalDate.now().format(formatter)
        val formatterBackend = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        backendDate = LocalDate.now().format(formatterBackend)
        binding.dateTxt.text = currentDate
        initViewModel()
        handleBack()
        // handleDateLo()
        handleSaveAttendanceBtn()
        observeClassesResponse()
        observeSectionsResponse()
        setupRecyclerView()
        setupObservers()
        observeAttendanceResponse()
        binding.checkBoxAll.setOnCheckedChangeListener(selectAllListener)
        var requestClasses = ClassTeacherApiRequest(
            "",
            userDetails[User.ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            "classes"
        )
        attendanceViewModel.fetchClasses(requestClasses)
        binding.parentNotificationCb.setOnCheckedChangeListener { _, isChecked ->
            notification_parent = if (isChecked) "yes" else "no"
            Log.d("NotifyValue", "Notify is: $notification_parent")
        }

    }

    private fun resetStudentsData() {
        currentPage = 1
        isLastPage = false
        isLoading = false
        studentsList.clear()
        selectedStudents.clear()
        binding.checkBoxAll.isChecked = false
        studentsAdapter.notifyDataSetChanged()
        binding.studentAttendanceRv.visibility = View.GONE
        binding.noDataTxt.visibility = View.VISIBLE
    }

    private fun fetchStudents() {
        val request = AttendanceStudentsApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID]!!,
            "",
            "",
            userDetails[User.SCHOOL_ID]!!,
            classId,
            backendDate,
            sectionId,
            selectedStudents,
            userDetails[User.ID]!!,
            "view",
            selectValue,
            currentPage,
            ""
        )
        attendanceViewModel.fetchStudents(request)
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreTutorials() {
        isLoading = true
        currentPage ++
        studentsAdapter.showLoadingFooter()
        fetchStudents()
    }

    private fun setupRecyclerView() {
        studentsAdapter = AttandanceStudentsAdapter(studentsList as ArrayList<Student?>)
        val linearLayoutManager = LinearLayoutManager(this)

        binding.studentAttendanceRv.apply {
            layoutManager = linearLayoutManager
            adapter = studentsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreTutorials()
                        }
                    }
                }
            })

            studentsAdapter.setupListener(object : OnAttendanceClickListener {
                override fun onAttendanceChanged(
                    selectedIds: ArrayList<AttendanceStudent>,
                    type: String
                ) {
                    selectValue = type
                    selectedStudents = selectedIds
                    Log.d("ATTENDANCE_LIST", "$selectedStudents , $selectValue")
                    binding.checkBoxAll.setOnCheckedChangeListener(null)
                    binding.checkBoxAll.isChecked = (type == "all")
                    binding.checkBoxAll.setOnCheckedChangeListener(selectAllListener)
                }

            })

        }

    }

    private fun setupObservers() {
        attendanceViewModel.studentsResponse.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.noDataTxt.visibility = View.GONE
                    binding.studentAttendanceRv.visibility = View.VISIBLE
                    binding.progress.hideProgress()
                    isLoading = false
                    attendanceStatus = state.data.attendance_status
                    studentsAdapter.attendanceStatus = attendanceStatus
                    studentsAdapter.removeLoadingFooter()
                    val newBookings = state.data.students
                    if (newBookings.isNotEmpty()) {

                        val startPosition = studentsList.size
                        studentsList.addAll(newBookings)

                        studentsAdapter.addPresentStudents(newBookings)

                        studentsAdapter.notifyItemRangeInserted(startPosition, newBookings.size)

                        if (isSelectAllChecked) {
                            studentsAdapter.selectAll()
                        }

                        if (newBookings.size < 10) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    studentsAdapter.removeLoadingFooter()
                    binding.progress.hideProgress()
                    ToastUtils.showErrorCustomToast(this, state.message)
                    if (state.message.equals("no data found", true)) {
                        binding.noDataTxt.visibility = View.VISIBLE
                        binding.studentAttendanceRv.visibility = View.GONE
                    }
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

    private fun observeAttendanceResponse() {
        attendanceViewModel.updateStudentsAttendanceResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    markAttendanceBinding.progress.showProgress()
                    markAttendanceBinding.markBtn.isEnabled = false
                    markAttendanceBinding.cancelBtn.isEnabled = false
                    markAttendanceBinding.crossIv.isEnabled = false

                }

                is UiState.Success -> {
                    markAttendanceBinding.progress.hideProgress()
                    ToastUtils.showSuccessCustomToast(this, "Attendance Marked Successfully..!")
                    resetStudentsData()
                    fetchStudents()
                    bottomSheetDialog.dismiss()

                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    markAttendanceBinding.progress.hideProgress()
                    markAttendanceBinding.markBtn.isEnabled = true
                    markAttendanceBinding.cancelBtn.isEnabled = true
                    markAttendanceBinding.crossIv.isEnabled = true
                }
            }
        }
    }

    private fun setupClassesAdapter(genderTypes: List<Class>) {
        var namesList = genderTypes.map { it.class_name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
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
                    resetStudentsData()
                    attendanceStatus = ""
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
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
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
                    resetStudentsData()
                    attendanceStatus = ""
                    if (!sectionId.equals("-1", true)) {
                        fetchStudents()
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun initViewModel() {
        val repository = AttendanceRepository(this)
        val factory = ViewModelFactory { AttendanceViewModel(repository) }
        attendanceViewModel = ViewModelProvider(this, factory)[AttendanceViewModel::class.java]
    }

    private fun handleSaveAttendanceBtn() {
        binding.saveAttendanceBtn.setOnClickListener(View.OnClickListener {

            if (classId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(this, "Please Select Class..!")
            } else if (sectionId.equals("-1", true)) {
                ToastUtils.showErrorCustomToast(this, "Please Select Section..!")
            } else if (selectedStudents.isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Select Students..!")
            } else {
                showAttendanceConformationBottomSheet()
            }
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showAttendanceConformationBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(this)
        markAttendanceBinding = AllStudentsPresentBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(markAttendanceBinding.root)
        bottomSheetDialog.setCanceledOnTouchOutside(false)

        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        markAttendanceBinding.cancelBtn.setOnClickListener {
            if (!markAttendanceBinding.markBtn.isEnabled) return@setOnClickListener
            bottomSheetDialog.dismiss()
        }

        markAttendanceBinding.crossIv.setOnClickListener {
            if (!markAttendanceBinding.markBtn.isEnabled) return@setOnClickListener
            bottomSheetDialog.dismiss()
        }

        markAttendanceBinding.markBtn.setOnClickListener {
            var markAttendanceRequest = AttendanceStudentsApiRequest(
                userDetails[User.ACADEMIC_YEAR_ID].toString(),
                "", "", userDetails[User.SCHOOL_ID].toString(),
                classId, backendDate, sectionId, selectedStudents,
                userDetails[User.ID].toString(), "insert", selectValue, 1, notification_parent!!
            )
            attendanceViewModel.updateStudentsAttendance(markAttendanceRequest)
            Log.d("MarkAttendanceRequest", markAttendanceRequest.toString())
        }

        bottomSheetDialog.show()
    }

}