package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iprism.school.R
import com.iprism.school.databinding.ActivityEditMealPlannerBinding
import com.iprism.school.databinding.ActivityStaffAttendanceBinding

class StaffAttendanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStaffAttendanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}