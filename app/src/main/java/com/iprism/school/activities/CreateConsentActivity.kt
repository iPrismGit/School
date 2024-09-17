package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCreateConsentBinding

class CreateConsentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateConsentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateConsentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
         })
    }

}