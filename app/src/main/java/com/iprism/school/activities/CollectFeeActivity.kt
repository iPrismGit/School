package com.iprism.school.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCollectFeeBinding
import com.iprism.school.model.fees.FeesApiResponse

class CollectFeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCollectFeeBinding
    private var studentId : String = ""
    private var sectionId : String = ""
    private var classId : String = ""
    private var className : String = ""
    private var sectionName : String = ""
    private var response: FeesApiResponse? =  null

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
    }

    fun getReceiptDate(): String = binding.receiptDateTxt.text.toString().trim()

    fun getReferenceNo(): String = binding.referenceNumberEt.text.toString().trim()

    fun getLateFee(): String = binding.lateFeeEt.text.toString().trim()

    fun getRemark(): String = binding.remarkEt.text.toString().trim()

    fun getAdminRemark(): String = binding.adminRemarkEt.text.toString().trim()

    fun getAmountTobeCollected(): String = binding.enterAmountToBeCollectEt.text.toString().trim()

    fun getDueDate(): String = binding.dueDateTxt.text.toString().trim()

    @SuppressLint("SetTextI18n")
    private fun setData() {
        binding.studentNameTxt.text = response?.response?.studentDetails?.firstName + " " + response?.response?.studentDetails?.middleName + " " + response?.response?.studentDetails?.lastName
        binding.classNameTxt.text = "$className - $sectionName"
        binding.receiptNoTxt.text = response?.response?.receiptNumber.toString()
        binding.lastReceiptNoTxt.text = "Last Receipt no : " + response?.response?.lastReceiptNumber.toString()
        binding.amountCollectedEt.text = response?.response?.paidAmount.toString()
        binding.balanceAmountEt.text = response?.response?.pendingAmount.toString()
    }
}