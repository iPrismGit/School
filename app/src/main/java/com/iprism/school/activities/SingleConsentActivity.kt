package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.R
import com.iprism.school.databinding.ActivitySingleConsentBinding

class SingleConsentActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleConsentBinding
    private var isInfoVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleConsentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleDownArrow()
    }

    private fun handleDownArrow() {
        binding.dropDownIv.setOnClickListener(View.OnClickListener {
            toggleInformationVisibility()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun toggleInformationVisibility() {
        if (isInfoVisible) {
            binding.infoLo.visibility = View.GONE
            binding.dropDownIv.setImageResource(R.drawable.down_arrow_img)
        } else {
            binding.infoLo.visibility = View.VISIBLE
            binding.dropDownIv.setImageResource(R.drawable.up_arrow_img)
        }
        isInfoVisible = !isInfoVisible
    }

}