package com.iprism.school.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.adapters.FeeTypesAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityStudentFeeDetailsBinding
import com.iprism.school.model.fees.FeeTypesItem
import com.iprism.school.model.fees.FeesRequest
import com.iprism.school.model.fees.Response
import com.iprism.school.repositories.FeesRepository
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.FeesViewModel
import com.iprism.school.viewModels.ViewModelFactory

class StudentFeeDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityStudentFeeDetailsBinding
    private var studentId : String = ""
    private var sectionId : String = ""
    private var classId : String = ""
    private var className : String = ""
    private var sectionName : String = ""
    private lateinit var viewModel: FeesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStudentFeeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor =
            ContextCompat.getColor(this, com.iprism.school.R.color.blue)
        WindowInsetsControllerCompat(
            window,
            window.decorView
        ).isAppearanceLightStatusBars = false
        if (intent.hasExtra("studentId")) {
            studentId = intent.getStringExtra("studentId").toString()
            sectionId = intent.getStringExtra("sectionId").toString()
            classId = intent.getStringExtra("classId").toString()
            className = intent.getStringExtra("className").toString()
            sectionName = intent.getStringExtra("sectionName").toString()
        }
        handleBack()
        initViewModel()
        fetchFees()
        observeFeesResponse()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

    private fun fetchFees() {
        val request = FeesRequest(
            sectionId,
            userDetails[User.ID]!!,
            userDetails[User.SCHOOL_ID]!!,
            classId,
            studentId
        )
        viewModel.fetchFees(request)
        Log.d("requestLoading", request.toString())
    }

    private fun initViewModel() {
        val repository = FeesRepository(this)
        val factory = ViewModelFactory { FeesViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[FeesViewModel::class.java]
    }

    private fun observeFeesResponse() {
        viewModel.response.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.dataLl.visibility = View.VISIBLE
                    binding.collectFeeBtn.visibility = View.VISIBLE
                    setData(state.data.response)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    if (state.message.equals("student fee is not yet configured", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                        binding.dataLl.visibility = View.GONE
                        binding.collectFeeBtn.visibility = View.GONE
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(response: Response) {
        binding.nameTxt.text = response.studentDetails.firstName + " " + response.studentDetails.middleName + " " + response.studentDetails.lastName
        binding.classTxt.text = "$className | Section $sectionName"
        binding.academicYearTxt.text = "Academic Year : " + response.studentDetails.academicYear.toString()
        binding.tvTotalAmount.text = "₹" + response.totalAmount.toString()
        binding.tvPaidAmount.text = "₹" + response.paidAmount.toString()
        binding.tvPendingAmount.text = "₹" + response.pendingAmount.toString()
        binding.itemsTxt.text = response.feeTypes.size.toString() + " Items"
        setupFeeTypesAdapter(response.feeTypes)
    }

    private fun setupFeeTypesAdapter(feeTypes: List<FeeTypesItem>) {
        val adapter = FeeTypesAdapter(this, feeTypes)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.feeTypesRv.adapter = adapter
        binding.feeTypesRv.layoutManager = linearLayoutManager
    }
}