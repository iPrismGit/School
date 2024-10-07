package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.R
import com.iprism.school.databinding.ActivityDaycareEmailReportBinding
import com.iprism.school.databinding.ActivitySetIconBinding
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils

class DaycareEmailReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaycareEmailReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaycareEmailReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleShareReportBtn()
        handleFromDateLo()
        handleToDateLo()
        handleBack()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleToDateLo() {
        binding.toDateLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(binding.toDateTxt, false)
        })
    }

    private fun handleFromDateLo() {
        binding.fromDateLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(binding.fromDateTxt, false)
        })
    }

    private fun handleShareReportBtn() {
        binding.shareReportBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Report Submitted Successfully")
            finish()
        })
    }

}