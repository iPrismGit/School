package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.R
import com.iprism.school.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleRequestOtp()
    }

    private fun handleRequestOtp() {
        binding.requestOtpBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, OtpVerificationActivity::class.java))
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}