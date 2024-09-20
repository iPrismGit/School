package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iprism.school.R
import com.iprism.school.databinding.ActivityStaffAttendanceBinding
import com.iprism.school.databinding.ActivityStaffAttendanceEmailReportBinding

class StaffAttendanceEmailReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStaffAttendanceEmailReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffAttendanceEmailReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}