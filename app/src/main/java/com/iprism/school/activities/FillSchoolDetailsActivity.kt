package com.iprism.school.activities

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.iprism.school.R
import com.iprism.school.databinding.ActivityFillSchoolDetailsBinding
import com.iprism.school.databinding.ActivityStudentDetailsBinding
import com.iprism.school.utils.ToastUtils

class FillSchoolDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFillSchoolDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillSchoolDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleNextBtn()
        handleBack()
        handleGenerateBtn()
    }

    private fun handleGenerateBtn() {
        binding.generateIdBtn.setOnClickListener(View.OnClickListener {
            blinkButton(binding.generateIdBtn)
            ToastUtils.showSuccessCustomToast(this, "ID Generated Successfully")
        })
    }

    private fun blinkButton(textView: TextView) {
        val blinkAnimation = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f)
        blinkAnimation.duration = 500 // 500ms for the blink
        blinkAnimation.repeatCount = 0 // Blink twice
        blinkAnimation.start()
    }

    private fun handleNextBtn() {
        binding.nextButton.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, FillPersonalDetailsActivity::class.java)
            startActivity(intent)
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}