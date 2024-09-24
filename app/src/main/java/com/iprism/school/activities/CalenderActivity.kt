package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.CalenderAdapter
import com.iprism.school.databinding.ActivityCalenderBinding

class CalenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleAddCalenderBtn()
        setupCalenderAdapter()
    }

    private fun setupCalenderAdapter() {
        var calenderAdapter = CalenderAdapter(this)
        binding.calendersIv.adapter = calenderAdapter
        var  linearLayoutManager = LinearLayoutManager(this)
        binding.calendersIv.layoutManager = linearLayoutManager
    }

    private fun handleAddCalenderBtn() {
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