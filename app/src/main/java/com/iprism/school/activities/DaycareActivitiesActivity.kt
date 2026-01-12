package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iprism.school.R
import com.iprism.school.databinding.ActivityDaycareActivitiesBinding

class DaycareActivitiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaycareActivitiesBinding
    private var planId = ""
    private var studentId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDaycareActivitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        planId = intent.getStringExtra("planId").toString()
        studentId = intent.getStringExtra("studentId").toString()
        handleBack()
        handleActivitiesLo()
        handleNapLo()
    }

    private fun handleNapLo() {
        binding.napsLo.setOnClickListener { view ->
            var intent = Intent(this, NapActivity::class.java)
            intent.putExtra("planId", planId)
            intent.putExtra("studentId", studentId)
            intent.putExtra("type", "nap")
            intent.putExtra("id", "7")
            startActivity(intent)
        }
    }

    private fun handleActivitiesLo() {
        binding.activityLo.setOnClickListener { view ->

        }
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

}