package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.adapters.DayCareReportsAdapter
import com.iprism.school.databinding.ActivityDaycareReportBinding
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils

class DaycareReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaycareReportBinding
    private var id: String = ""
    private var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaycareReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getStringExtra("id").toString()
        name = intent.getStringExtra("name").toString()
        binding.dayCareNameTxt.text = name
        handleBack()
        handleDateLo()
        handleStartDateLo()
        handleEndTimeLo()
        handleSendBtn()
    }

    private fun handleSendBtn() {
        binding.sendBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, name + "Successfully Uploaded")
            finish()
        })
    }

    private fun handleEndTimeLo() {
        binding.endTimeLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getTime(binding.endTimeTxt)
        })
    }

    private fun handleStartDateLo() {
        binding.startTimeLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getTime(binding.startTimeTxt)
        })
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

}