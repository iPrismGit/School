package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCalenderBinding

class CalenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        hanldeAddCalenderBtn()
    }

    private fun hanldeAddCalenderBtn() {
        binding.addCalenderBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CreateCalenderActivity::class.java))
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}