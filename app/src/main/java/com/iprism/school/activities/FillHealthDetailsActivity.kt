package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iprism.school.R
import com.iprism.school.databinding.ActivityFillHealthDetailsBinding
import com.iprism.school.databinding.ActivityFillOtherDetailsBinding

class FillHealthDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFillHealthDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillHealthDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}