package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.R
import com.iprism.school.databinding.ActivityDayCareReportsBinding
import com.iprism.school.databinding.ActivityDaycareReportBinding

class DayCareReportsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDayCareReportsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDayCareReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}