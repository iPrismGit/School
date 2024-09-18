package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.iprism.school.R
import com.iprism.school.databinding.ActivityConsentEmailReportBinding
import com.iprism.school.databinding.ActivityConsentInfoBinding

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
            Toast.makeText(this, "Report Shared..!", Toast.LENGTH_SHORT).show()
            finish()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}