package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.R
import com.iprism.school.databinding.ActivityFillPersonalDetailsBinding
import com.iprism.school.utils.DateTimeUtils

class FillPersonalDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFillPersonalDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillPersonalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleDateOfBirthLo()
        handleDateOfJoiningLo()
        handleNext()
    }

    private fun handleNext() {
        binding.nextButton.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, FillParentDetailsActivity::class.java);
            startActivity(intent)
        })
    }

    private fun handleDateOfJoiningLo() {
        binding.dateOfJoiningCvLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(binding.dateOfJoiningTxt, true)
        })
    }

    private fun handleDateOfBirthLo() {
        binding.dateOfBirthCvLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(binding.dateOfBirthTxt, true)
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}