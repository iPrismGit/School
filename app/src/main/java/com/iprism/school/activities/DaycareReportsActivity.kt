package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.DayCareReportsAdapter
import com.iprism.school.databinding.ActivityDaycareReportsBinding
import com.iprism.school.databinding.DaycareReportItemBinding
import com.iprism.school.utils.DateTimeUtils

class DaycareReportsActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityDaycareReportsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaycareReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDaycareReportsAdapter()
        handleBack()
        handleDateLo()
    }

    private fun handleDateLo() {
        binding.dateLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(binding.dateTxt, false)
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun setupDaycareReportsAdapter() {
        var dayCareReportsAdapter = DayCareReportsAdapter(this)
        binding.dayCaresRv.adapter = dayCareReportsAdapter
        var layoutManager = LinearLayoutManager(this)
        binding.dayCaresRv.layoutManager = layoutManager
    }

}