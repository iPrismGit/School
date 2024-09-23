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

    @SuppressLint("ResourceAsColor")
    private fun handleRejectedLo() {
        binding.rejectedLo.setOnClickListener(View.OnClickListener {
            binding.rejectedTxt.setTextColor(R.color.blue)
            binding.rejectedCountTxt.setTextColor(R.color.blue)
            binding.pendingTxt.setTextColor(R.color.blue)
            binding.pendingCountTxt.setTextColor(R.color.blue)
            attendanceType = "rejected"
        })
    }

    @SuppressLint("ResourceAsColor")
    private fun handlePendingLo() {
        binding.pendingLo.setOnClickListener(View.OnClickListener {
            binding.pendingTxt.setTextColor(R.color.blue)
            binding.pendingCountTxt.setTextColor(R.color.blue)
            binding.rejectedTxt.setTextColor(R.color.blue)
            binding.rejectedCountTxt.setTextColor(R.color.blue)
            attendanceType = "pending"
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}