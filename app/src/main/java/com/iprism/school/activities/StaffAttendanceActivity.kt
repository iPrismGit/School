package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iprism.school.R
import com.iprism.school.databinding.ActivityStaffAttendanceBinding

class StaffAttendanceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStaffAttendanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStaffAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleAddBtn()
        handleBackBtn()
    }

    private fun handleBackBtn() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

    private fun handleAddBtn() {
        binding.addAttendanceBtn.setOnClickListener { view ->
            startActivity(Intent(this, AddAttendanceActivity::class.java))
        }
    }
}