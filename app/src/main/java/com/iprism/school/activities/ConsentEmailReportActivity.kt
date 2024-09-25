package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.databinding.ActivityConsentEmailReportBinding
import com.iprism.school.utils.ToastUtils

class ConsentEmailReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsentEmailReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsentEmailReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleShareReportBtn()
    }



    private fun handleShareReportBtn() {
        binding.shareReportBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Report Emailed Successfully")
            finish()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}