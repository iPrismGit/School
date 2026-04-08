package com.iprism.school.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.adapters.StudentLeaveRequestsAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityStudentLeaveRequestsBinding
import com.iprism.school.databinding.AllStudentsPresentBottomSheetBinding
import com.iprism.school.databinding.LeaveApprovalOrRejectionBottomSheetBinding
import com.iprism.school.interfaces.OnLeaveRequestClickListener
import com.iprism.school.model.classteachermodel.AttendanceStudentsApiRequest
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
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var approvalBottomSheetBinding: LeaveApprovalOrRejectionBottomSheetBinding

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
        observeLeaveRequestUpdateResponse()
        val requestClasses = ClassTeacherApiRequest(
            "",
            userDetails[User.ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            "classes")
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
                    if (result.data.response.requests.isNotEmpty()) {
                        binding.leaveRequestsRv.visibility = View.VISIBLE
                        setupLeaveRequestsAdapter(result.data.response.requests)
                        binding.noDataFoundTxt.visibility = View.GONE
                    } else {
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

    private fun observeLeaveRequestUpdateResponse() {
        leaveRequestsViewModel.updateLeaveRequestsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    approvalBottomSheetBinding.progress.showProgress()
                    approvalBottomSheetBinding.approveBtn.isEnabled = false
                    approvalBottomSheetBinding.rejectBtn.isEnabled = false
                    approvalBottomSheetBinding.crossIv.isEnabled = false
                }

                is UiState.Success -> {
                    approvalBottomSheetBinding.progress.hideProgress()
                    fetchLeaveRequests()
                    bottomSheetDialog.dismiss()
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    approvalBottomSheetBinding.progress.hideProgress()
                    approvalBottomSheetBinding.approveBtn.isEnabled = true
                    approvalBottomSheetBinding.rejectBtn.isEnabled = true
                    approvalBottomSheetBinding.crossIv.isEnabled = true
                }
            }
        }
    }

    private fun setupLeaveRequestsAdapter(leaveRequests: List<Request>) {
        var adapter = StudentLeaveRequestsAdapter(leaveRequests)
        binding.leaveRequestsRv.adapter = adapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.leaveRequestsRv.layoutManager = linearLayoutManager
        adapter.setupListener(object : OnLeaveRequestClickListener {
            override fun onItemClick(leaveRequestId: String, status: String) {
                if (status.isEmpty()) {
                    showLeaveApprovalBottomSheet(leaveRequestId)
                }

            }

            override fun onViewAttachmentClick(attachmentUrl: String) {
                if (attachmentUrl.isNotEmpty()) {
                    if (attachmentUrl.endsWith(".pdf")) {
                        var intent =
                            Intent(this@StudentLeaveRequestsActivity, PdfViewActivity::class.java)
                        intent.putExtra("pdfUrl", attachmentUrl)
                        startActivity(intent)
                    } else {
                        var intent =
                            Intent(this@StudentLeaveRequestsActivity, ViewImageActivity::class.java)
                        intent.putExtra("EventImage", attachmentUrl)
                        intent.putExtra("EventName", "Leave Request Image")
                        startActivity(intent)
                    }
                }
            }

        })
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
                    sectionId = "-1"
                    binding.sectionsSp.setSelection(0)

                    binding.leaveRequestsRv.visibility = View.GONE
                    binding.noDataFoundTxt.visibility = View.GONE

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
                        fetchLeaveRequests()
                    } else {
                        binding.leaveRequestsRv.visibility = View.GONE
                        binding.noDataFoundTxt.visibility = View.GONE
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun fetchLeaveRequests() {
        var request = LeaveRequestApiRequest(
            userDetails[User.SCHOOL_ID].toString(),
            classId,
            "",
            sectionId,
            "",
            userDetails[User.ID].toString(),
            "view",
            ""
        )
        leaveRequestsViewModel.fetchLeaveRequests(request)
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

    private fun showLeaveApprovalBottomSheet(leaveRequestId: String) {
        bottomSheetDialog = BottomSheetDialog(this)
        approvalBottomSheetBinding =
            LeaveApprovalOrRejectionBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(approvalBottomSheetBinding.root)
        bottomSheetDialog.setCanceledOnTouchOutside(false)

        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        approvalBottomSheetBinding.rejectBtn.setOnClickListener {
            if (approvalBottomSheetBinding.reasonTxt.text.toString().trim().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Reason for Rejection..!")
            } else {
                var request = LeaveRequestApiRequest(
                    userDetails[User.SCHOOL_ID].toString(),
                    userDetails[User.ACADEMIC_YEAR_ID].toString(),
                    leaveRequestId,
                    sectionId,
                    "rejected",
                    userDetails[User.ID].toString(),
                    "update",
                    approvalBottomSheetBinding.reasonTxt.text.toString().trim()
                )
                leaveRequestsViewModel.updateLeaveRequests(request)
                Log.d("RejectionRequest", request.toString())
            }
        }

        approvalBottomSheetBinding.crossIv.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        approvalBottomSheetBinding.approveBtn.setOnClickListener {
            var request = LeaveRequestApiRequest(
                userDetails[User.SCHOOL_ID].toString(),
                userDetails[User.ACADEMIC_YEAR_ID].toString(),
                leaveRequestId,
                sectionId,
                "accepted",
                userDetails[User.ID].toString(),
                "update",
                ""
            )
            leaveRequestsViewModel.updateLeaveRequests(request)
            Log.d("ApprovalRequest", request.toString())
        }

        bottomSheetDialog.show()
    }

}