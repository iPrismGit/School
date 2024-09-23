package com.iprism.school.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.R
import com.iprism.school.databinding.ActivityAttendanceBinding
import com.iprism.school.databinding.ActivityStaffAttendanceBinding

class AttendanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAttendanceBinding
    private var attendanceType: String = "pending"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handlePendingLo()
        handleRejectedLo()
    }

    private fun handleRejectedLo() {
        binding.rejectedLo.setOnClickListener(View.OnClickListener {
            binding.rejectedTxt.setTextColor(resources.getColor(R.color.blue3))
            binding.rejectedCountTxt.setTextColor(resources.getColor(R.color.blue3))
            binding.pendingTxt.setTextColor(resources.getColor(R.color.gray1))
            binding.pendingCountTxt.setTextColor(resources.getColor(R.color.gray1))
            attendanceType = "rejected"
        })
    }

    private fun handlePendingLo() {
        binding.pendingLo.setOnClickListener(View.OnClickListener {
            binding.pendingTxt.setTextColor(resources.getColor(R.color.blue3))
            binding.pendingCountTxt.setTextColor(resources.getColor(R.color.blue3))
            binding.rejectedTxt.setTextColor(resources.getColor(R.color.gray1))
            binding.rejectedCountTxt.setTextColor(resources.getColor(R.color.gray1))
            attendanceType = "pending"
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}