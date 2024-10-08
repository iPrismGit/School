package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iprism.school.R
import com.iprism.school.databinding.ActivityFillSchoolDetailsBinding
import com.iprism.school.databinding.ActivityStudentDetailsBinding

class FillSchoolDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFillSchoolDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillSchoolDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}