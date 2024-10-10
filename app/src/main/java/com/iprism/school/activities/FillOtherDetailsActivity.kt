package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iprism.school.R
import com.iprism.school.databinding.ActivityFillOtherDetailsBinding
import com.iprism.school.databinding.ActivityFillParentDetailsBinding
import com.iprism.school.databinding.ActivityFillSchoolDetailsBinding

class FillOtherDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFillOtherDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillOtherDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
    }

    private fun handleBack() {

    }

}