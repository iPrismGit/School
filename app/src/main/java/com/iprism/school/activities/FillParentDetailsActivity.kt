package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.R
import com.iprism.school.databinding.ActivityFillParentDetailsBinding
import com.iprism.school.databinding.ActivityFillPersonalDetailsBinding

class FillParentDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFillParentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillParentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleNext()
    }

    private fun handleNext() {
        binding.nextButton.setOnClickListener(View.OnClickListener {

        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}