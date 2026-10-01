package com.iprism.school.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.iprism.school.R
import com.iprism.school.databinding.ActivityStudentFeeDetailsBinding

class StudentFeeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentFeeDetailsBinding
    private var studentId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStudentFeeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor =
            ContextCompat.getColor(this, com.iprism.school.R.color.blue)
        WindowInsetsControllerCompat(
            window,
            window.decorView
        ).isAppearanceLightStatusBars = false
        if (intent.hasExtra("studentId")) {
            studentId = intent.getStringExtra("studentId").toString()
        }
        handleBack()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }
}