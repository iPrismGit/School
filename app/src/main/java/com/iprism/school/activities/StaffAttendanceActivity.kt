package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.StaffAttendancesAdapter
import com.iprism.school.databinding.ActivityEditMealPlannerBinding
import com.iprism.school.databinding.ActivityStaffAttendanceBinding

class StaffAttendanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStaffAttendanceBinding
    private var selectedType: String = "Marked"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleEmailIv()
        setupStaffAttendanceAdapter()
        setupRadioButtonListener(binding.staffRadioGroup)
    }

    private fun handleEmailIv() {
        binding.emailIv.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, StaffAttendanceEmailReportActivity::class.java))
        })
    }

    private fun setupStaffAttendanceAdapter() {
        var staffAttendancesAdapter = StaffAttendancesAdapter(this)
        binding.staffAttendanceRv.adapter = staffAttendancesAdapter
        var layoutManager = LinearLayoutManager(this)
        binding.staffAttendanceRv.layoutManager = layoutManager
    }

    private fun setupRadioButtonListener(radioGroup: RadioGroup) {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.marked_staff_rb -> {
                    selectedType = "Marked"
                    Toast.makeText(this, "Selected: $selectedType", Toast.LENGTH_SHORT).show()
                }

                R.id.not_marked_staff_rb -> {
                    selectedType = "Not Marked"
                    Toast.makeText(this, "Selected: $selectedType", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}