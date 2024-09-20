package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.iprism.school.databinding.ActivityEditConsentBinding
import com.iprism.school.utils.ToastUtils

class EditConsentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditConsentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditConsentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleSendBtn()
        handleBack()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleSendBtn() {
        binding.sendBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Consent Edited Successfully")
            finish()
        })
    }

}