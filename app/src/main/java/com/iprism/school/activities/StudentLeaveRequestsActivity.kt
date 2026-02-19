package com.iprism.school.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.StudentLeaveRequestsAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityStudentLeaveRequestsBinding
import com.iprism.school.model.classteachermodel.Class
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.Section
import com.iprism.school.model.leaverequestmodel.LeaveRequestApiRequest
import com.iprism.school.model.leaverequestmodel.Request
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.LeaveRequestRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.LeaveRequestsViewModel
import com.iprism.school.viewModels.ViewModelFactory

class StudentLeaveRequestsActivity : BaseActivity() {

    private lateinit var binding: ActivityStudentLeaveRequestsBinding
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var leaveRequestsViewModel: LeaveRequestsViewModel
    private var classId: String = "-1"
    private var sectionId: String = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStudentLeaveRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViewModel()
        observeClassesResponse()
        observeSectionsResponse()
        observeLeaveRequestsResponse()
        var requestClasses = ClassTeacherApiRequest(
            "",
            userDetails[User.ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            "classes"
        )
        attendanceViewModel.fetchClasses(requestClasses)
        handleBack()

    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
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

    private fun observeLeaveRequestsResponse() {
        leaveRequestsViewModel.leaveRequestsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.leaveRequestsRv.visibility = View.GONE
                    binding.noDataFoundTxt.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.response.requests.isNotEmpty()){
                        binding.leaveRequestsRv.visibility = View.VISIBLE
                        setupLeaveRequestsAdapter(result.data.response.requests)
                        binding.noDataFoundTxt.visibility = View.GONE
                    }else{
                        binding.leaveRequestsRv.visibility = View.GONE
                        binding.noDataFoundTxt.visibility = View.VISIBLE
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                    binding.leaveRequestsRv.visibility = View.GONE
                    binding.noDataFoundTxt.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupLeaveRequestsAdapter(leaveRequests: List<Request>){
        var adapter = StudentLeaveRequestsAdapter(leaveRequests)
        binding.leaveRequestsRv.adapter = adapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.leaveRequestsRv.layoutManager = linearLayoutManager
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
                    if (!sectionId.equals("-1", true)) {
                        var request = LeaveRequestApiRequest(
                            userDetails[User.SCHOOL_ID].toString(),
                            classId,
                            "",
                            sectionId,
                            "",
                            userDetails[User.ID].toString(),
                            "view"
                        )
                        leaveRequestsViewModel.fetchLeaveRequests(request)
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

        val leaveRequestRepository = LeaveRequestRepository(this)
        val leaveRequestFactory =
            ViewModelFactory { LeaveRequestsViewModel(leaveRequestRepository) }
        leaveRequestsViewModel =
            ViewModelProvider(this, leaveRequestFactory)[LeaveRequestsViewModel::class.java]

    }

}