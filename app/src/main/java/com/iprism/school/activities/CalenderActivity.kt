package com.iprism.school.activities

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.iprism.school.R
import com.iprism.school.adapters.EventsPagerAdapter
import com.iprism.school.adapters.HomePagerAdapter
import com.iprism.school.adapters.MessagesPagerAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityCalenderBinding

@RequiresApi(Build.VERSION_CODES.O)
class CalenderActivity : BaseActivity() {

    private lateinit var binding: ActivityCalenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        val adapter = EventsPagerAdapter(this)
        binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.adapter = adapter
        binding.viewPager2.setCurrentItem(0, false)
        setupButtonsStyling(binding.classesBtn, binding.daycareBtn)
        handleClassesBtn()
        handleDayCareBtn()
    }

    private fun handleClassesBtn() {
        binding.classesBtn.setOnClickListener { v ->
            binding.viewPager2.setCurrentItem(0, false)
            setupButtonsStyling(binding.classesBtn, binding.daycareBtn)
        }
    }

    private fun handleDayCareBtn() {
        binding.daycareBtn.setOnClickListener { v ->
            binding.viewPager2.setCurrentItem(1, false)
            setupButtonsStyling(binding.daycareBtn, binding.classesBtn)
        }
    }

    private fun setupButtonsStyling(
        classesBtn: TextView,
        daycareBtn: TextView
    ) {
        classesBtn.setTextColor(ContextCompat.getColor(this, R.color.white))
        classesBtn.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.filled_button_bg))
        daycareBtn.setTextColor(ContextCompat.getColor(this, R.color.blue1))
        daycareBtn.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_outline_button))
    }

}