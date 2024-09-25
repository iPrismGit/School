package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iprism.school.R
import com.iprism.school.databinding.ActivityDaycareEmailReportBinding
import com.iprism.school.databinding.ActivitySetIconBinding

class DaycareEmailReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaycareEmailReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaycareEmailReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}