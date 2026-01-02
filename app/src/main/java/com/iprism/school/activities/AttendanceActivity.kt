package com.iprism.school.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.adapters.AttandanceStudentsAdapter
import com.iprism.school.adapters.StudentsAttendanceAdapter
import com.iprism.school.databinding.ActivityAttendanceBinding
import com.iprism.school.interfaces.OnAttendanceClickListener
import com.iprism.school.model.Response.ClasseList
import com.iprism.school.model.classteachermodel.AttendanceStudent
import com.iprism.school.model.classteachermodel.AttendanceStudentsApiRequest
import com.iprism.school.model.classteachermodel.Class
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassesResponse
import com.iprism.school.model.classteachermodel.Section
import com.iprism.school.model.classteachermodel.SectionsResponse
import com.iprism.school.model.classteachermodel.Student
import com.iprism.school.repositories.AttendanceRepository
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
    private val selectedStudentIds = mutableSetOf<String>()
    private var isSelectAllChecked = false
    private var selectedAttendanceList = mutableListOf<AttendanceStudent>()

    private lateinit var studentsAdapter: AttandanceStudentsAdapter
    private var studentsList = mutableListOf<Student>()
    private var selectedStudentsList = mutableListOf<AttendanceStudent>()
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 5
    private var classId: String = "-1"
    private var sectionId: String = "-1"
    private lateinit var attendanceCrossImage: ImageView
    private lateinit var markBtn: Button
    private lateinit var attendanceCancelBtn: Button
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var currentDate: String = ""
    private var backendDate: String = ""
    private var academicYear: String = ""
    private var academicYearId: String = ""
    private var selected_class_ids: String? = ""
    private var notification_parent: String? = ""
    private val selectAllListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            studentsAdapter.selectAll(isChecked)
        }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()
        val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")
        currentDate = LocalDate.now().format(formatter)

        val formatterBackend = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        backendDate = LocalDate.now().format(formatterBackend)
        binding.dateTxt.text = currentDate
        initViewModel()
        handleBack()
        handleDateLo()
        handleSaveAttendanceBtn()
        observeAcademicYearsResponse()
        observeClassesResponse()
        observeSectionsResponse()
        setupRecyclerView()
        observeStudentsResponse()
        handleRefreshLo()
        binding.checkBoxAll.setOnCheckedChangeListener(selectAllListener)
        var request = ClassTeacherApiRequest("", userDetails[User.ID].toString(), "academic_year")
        attendanceViewModel.fetchAcademicYears(request)
        var requestClasses = ClassTeacherApiRequest("", userDetails[User.ID].toString(), "classes")
        attendanceViewModel.fetchClasses(requestClasses)
        Log.d("ClassRequest", request.toString())
        binding.parentNotificationCb.setOnCheckedChangeListener { _, isChecked ->
            notification_parent = if (isChecked) "yes" else "no"
            Log.d("NotifyValue", "Notify is: $notification_parent")
        }

    }

    private fun loadStudents() {
        var request = AttendanceStudentsApiRequest(
            academicYearId, "", "",
            userDetails[User.SCHOOL_ID].toString(), classId, backendDate, sectionId,
            selectedStudentsList, userDetails[User.ID].toString(), "view", currentPage
        )
        attendanceViewModel.fetchStudents(request)
        Log.d("StudentsFetchRequest", request.toString())
    }

    private fun refreshItems() {
        currentPage = 1
        isLastPage = false
        isLoading = false
        studentsList.clear()
        studentsAdapter.notifyDataSetChanged()
        loadStudents()
    }

    private fun loadMoreItems() {
        if (isLastPage || isLoading) return
        isLoading = true
        currentPage++
        loadStudents()
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
        studentsAdapter = AttandanceStudentsAdapter(this, studentsList)
        val linearLayoutManager = LinearLayoutManager(this)

        binding.studentAttendanceRv.apply {
            layoutManager = linearLayoutManager
            adapter = studentsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled =
                        !binding.studentAttendanceRv.canScrollVertically(-1)
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

            studentsAdapter.setupListener(object : OnAttendanceClickListener {

                override fun onAttendanceChanged(
                    selectedIds: List<String>,
                    isAllSelected: Boolean
                ) {

                    binding.checkBoxAll.setOnCheckedChangeListener(null)
                    binding.checkBoxAll.isChecked = isAllSelected
                    binding.checkBoxAll.setOnCheckedChangeListener(selectAllListener)

                    // store selected ids
                    selectedAttendanceList.clear()
                    selectedIds.forEach {
                        selectedAttendanceList.add(AttendanceStudent(it))
                    }

                    Log.d("SelectedIdsList", selectedAttendanceList.toString())
                }

            })

        }

    }

    private fun observeStudentsResponse() {
        attendanceViewModel.studentsResponse.observe(this) { result ->

            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false
                    attendanceStatus = result.data.attendance_status
                    val newStudents = result.data.students

                    if (newStudents.isNotEmpty()) {
                        selectedStudentsList = studentsList
                            .filter { it.isSelected }
                            .map { AttendanceStudent(it.id) }
                            .toMutableList()
                        if (isSelectAllChecked) {
                            newStudents.forEach { it.isSelected = true }
                        }
                        studentsList.addAll(newStudents)
                        studentsAdapter.notifyDataSetChanged()
                        isLastPage = newStudents.size < limit
                        binding.studentAttendanceRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                    } else {
                        isLastPage = true

                        if (currentPage == 1) {
                            studentsList.clear()
                            studentsAdapter.notifyDataSetChanged()
                            binding.studentAttendanceRv.visibility = View.GONE
                            binding.noDataTxt.visibility = View.VISIBLE
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    if (studentsList.isEmpty()) {
                        binding.studentAttendanceRv.visibility = View.GONE
                        binding.noDataTxt.visibility = View.VISIBLE
                        ToastUtils.showErrorCustomToast(this, result.message)
                    } else {
                        binding.studentAttendanceRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                        ToastUtils.showErrorCustomToast(this, "There is no more data")
                    }
                }
            }
        }
    }

    private fun observeAcademicYearsResponse() {
        attendanceViewModel.academicYearsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    academicYear = result.data.name
                    academicYearId = result.data.id
                    Log.d("AcademicYear", academicYear + ", " + academicYearId)
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    Log.d("Message", result.message)
                    binding.progress.hideProgress()
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
                    if (!classId.equals("-1", true)) {
                        var requestClasses = ClassTeacherApiRequest(
                            classId,
                            userDetails[User.ID].toString(),
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
                    if (!sectionId.equals("-1", true)) {
                        loadStudents()
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun initViewModel() {
        val repository = AttendanceRepository()
        val factory = ViewModelFactory { AttendanceViewModel(repository) }
        attendanceViewModel = ViewModelProvider(this, factory)[AttendanceViewModel::class.java]
    }

    private fun handleDateLo() {
        binding.dateLo.setOnClickListener { view ->
            DateTimeUtils.getDate(binding.dateTxt, true)
        }
    }

    private fun handleSaveAttendanceBtn() {
        binding.saveAttendanceBtn.setOnClickListener(View.OnClickListener {

            selectedStudentsList = selectedStudentIds.map {
                AttendanceStudent(id = it)
            }.toMutableList()

            //showAttendanceConformationBottomSheet()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AttendanceActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    private fun showAttendanceConformationBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View =
            LayoutInflater.from(this).inflate(R.layout.all_students_present_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        attendanceCancelBtn = bottomSheetDialog.findViewById<View>(R.id.cancel_btn) as Button
        attendanceCrossImage = bottomSheetDialog.findViewById<View>(R.id.cross_iv) as ImageView
        markBtn = bottomSheetDialog.findViewById<View>(R.id.mark_button) as Button
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }

        attendanceCancelBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        attendanceCrossImage.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        markBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
            ToastUtils.showSuccessCustomToast(this, "Attendance Marked Successfully")
        })

        bottomSheetDialog.show()
    }

    @SuppressLint("GestureBackNavigation")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@AttendanceActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}