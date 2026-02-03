package com.iprism.school.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.school.R
import com.iprism.school.adapters.CalenderAdapter
import com.iprism.school.adapters.LeaveRequestsAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityLeaveRequestsBinding
import com.iprism.school.interfaces.OnCalenderClickListener
import com.iprism.school.model.applyforleavemodel.ApplyForLeaveApiRequest
import com.iprism.school.model.applyforleavemodel.LeaveRequest
import com.iprism.school.model.classteachermodel.Class
import com.iprism.school.model.eventsmodel.Event
import com.iprism.school.model.eventsmodel.EventsApiRequest
import com.iprism.school.repositories.ApplyForLeaveRepository
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.EventsRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.ApplyForLeaveViewModel
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.EventsViewModel
import com.iprism.school.viewModels.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LeaveRequestsActivity : BaseActivity() {

    private lateinit var binding: ActivityLeaveRequestsBinding
    private lateinit var viewModel: ApplyForLeaveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLeaveRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViewModel()
        observeLeaveRequestsResponse()
        var request = ApplyForLeaveApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            userDetails[User.SCHOOL_ID].toString(), "", "", "",
            "", "", userDetails[User.ID].toString(), "view"
        )
        viewModel.fetchLeaveRequests(request)
    }

    private fun initViewModel() {
        val repository = ApplyForLeaveRepository(this)
        val factory = ViewModelFactory { ApplyForLeaveViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[ApplyForLeaveViewModel::class.java]

    }

    @SuppressLint("SuspiciousIndentation")
    private fun observeLeaveRequestsResponse() {
        viewModel.leaveRequestsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.leaveRequestsRv.visibility = View.GONE
                    binding.noDataLo.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.leave_requests.isNotEmpty()) {
                        setupLeaveRequestsAdapter(result.data.leave_requests)
                        binding.leaveRequestsRv.visibility = View.VISIBLE
                        binding.noDataLo.visibility = View.GONE

                    } else {
                        binding.leaveRequestsRv.visibility = View.GONE
                        binding.noDataLo.visibility = View.VISIBLE
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                    binding.leaveRequestsRv.visibility = View.GONE
                    binding.noDataLo.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupLeaveRequestsAdapter(leaveRequests: List<LeaveRequest>) {
        var leaveRequestsAdapter = LeaveRequestsAdapter(leaveRequests)
        var linearLayoutManager = LinearLayoutManager(this)
        binding.leaveRequestsRv.adapter = leaveRequestsAdapter
        binding.leaveRequestsRv.layoutManager = linearLayoutManager
        leaveRequestsAdapter.setupListener(object : OnCalenderClickListener {
            override fun onItemClick(
                calenderId: String,
                calenderName: String,
                image: String
            ) {
                if (image.isNotEmpty()) {
                    var intent = Intent(this@LeaveRequestsActivity, ViewImageActivity::class.java)
                    intent.putExtra("EventImage", image)
                    intent.putExtra("EventName", "Leave Request")
                    startActivity(intent)
                } else {
                    ToastUtils.showErrorCustomToast(this@LeaveRequestsActivity, "No Image Found..!")
                }
            }

        })
    }

}