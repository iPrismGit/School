package com.iprism.school.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityCollectFeeBinding
import com.iprism.school.model.fees.FeesApiResponse
import com.iprism.school.model.insertfee.InsertFeeRequest
import com.iprism.school.repositories.FeesRepository
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.InsertFeeViewModel
import com.iprism.school.viewModels.ViewModelFactory

class CollectFeeActivity : BaseActivity() {

    private lateinit var binding: ActivityCollectFeeBinding
    private var studentId: String = ""
    private var sectionId: String = ""
    private var classId: String = ""
    private var className: String = ""
    private var sectionName: String = ""
    private var paymentType: String = ""
    private var response: FeesApiResponse? = null
    private val paymentTypes = listOf("Select Payment Mode", "Cash", "UPI", "Card", "Online")
    private lateinit var viewModel: InsertFeeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCollectFeeBinding.inflate(layoutInflater)
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
            response = intent.getSerializableExtra("response") as FeesApiResponse
            setData()
        }
        setupPaymentTypesSpinner()
        handleDueDate()
        handleReceiptDate()
        handlePaymentType()
        initViewModel()
        observeFeesResponse()
        handleSubmit()
    }

    fun getReceiptDate(): String = binding.receiptDateTxt.text.toString().trim()

    fun getReferenceNo(): String = binding.referenceNumberEt.text.toString().trim()

    fun getLateFee(): String = binding.lateFeeEt.text.toString().trim()

    fun getRemark(): String = binding.remarkEt.text.toString().trim()

    fun getAdminRemark(): String = binding.adminRemarkEt.text.toString().trim()

    fun getAmountTobeCollected(): String = binding.enterAmountToBeCollectEt.text.toString().trim()

    @SuppressLint("SetTextI18n")
    private fun setData() {
        binding.studentNameTxt.text =
            response?.response?.studentDetails?.firstName + " " + response?.response?.studentDetails?.middleName + " " + response?.response?.studentDetails?.lastName
        binding.classNameTxt.text = "$className - $sectionName"
        binding.receiptNoTxt.text = response?.response?.receiptNumber.toString()
        binding.lastReceiptNoTxt.text =
            "Last Receipt no : " + response?.response?.lastReceiptNumber.toString()
        binding.amountCollectedEt.text = response?.response?.paidAmount.toString()
        binding.balanceAmountEt.text = response?.response?.pendingAmount.toString()
    }

    private fun setupPaymentTypesSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.paymentTypeSpinner.adapter = adapter
    }

    private fun handleReceiptDate() {
        binding.receiptDateLl.setOnClickListener {
            DateTimeUtils.getDateYmd(binding.receiptDateTxt, true)
        }
    }

    private fun handleDueDate() {
        binding.dueDateLl.setOnClickListener {
            DateTimeUtils.getDate(binding.dueDateTxt, true)
        }
    }

    private fun handlePaymentType() {
        binding.paymentTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    paymentType = parent?.getItemAtPosition(position).toString()
                    if (paymentType == "Online" || paymentType == "Card" || paymentType == "UPI") {
                        binding.referenceNumberEt.visibility = View.VISIBLE
                        binding.refernceTxt.visibility = View.VISIBLE
                    } else {
                        binding.referenceNumberEt.visibility = View.GONE
                        binding.refernceTxt.visibility = View.GONE
                        binding.referenceNumberEt.setText("")
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    private fun handleSubmit() {
        binding.submitBtn.setOnClickListener {
            insertFee()
        }
    }

    private fun insertFee() {
        if (getReceiptDate().isEmpty()) {
            showToast("Please Select Receipt Date")
        } else if (paymentType == "Select Payment Mode") {
            showToast("Please Select Payment Mode")
        } else if (getAmountTobeCollected().isEmpty() || getAmountTobeCollected() == "0") {
            showToast("Please Enter Amount to be Collect")
        } else {
            val request = InsertFeeRequest(
                paymentType,
                getAmountTobeCollected(),
                getLateFee(),
                "",
                classId,
                getAdminRemark(),
                "No need",
                studentId,
                getRemark(),
                "",
                "",
                getReferenceNo(),
                response!!.response.receiptNumber.toString(),
                sectionId,
                response!!.response.studentDetails.academicYear.toString(),
                userDetails[User.ID].toString(),
                userDetails[User.SCHOOL_ID].toString(),
                "",
                getReceiptDate(),
                response!!.response.configurationId.toString(),
            )
            viewModel.insertFee(request)
            Log.d("requestLoading", request.toString())
        }
    }

    private fun initViewModel() {
        val repository = FeesRepository(this)
        val factory = ViewModelFactory { InsertFeeViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[InsertFeeViewModel::class.java]
    }

    private fun observeFeesResponse() {
        viewModel.response.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    showToast("Payment Inserted Successfully")
                    finish()
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    showToast(state.message)
                }
            }
        }
    }
}