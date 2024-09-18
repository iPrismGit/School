package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.iprism.school.databinding.ActivityEditConsentBinding

class EditConsentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditConsentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditConsentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleSendBtn()
    }

    private fun handleSendBtn() {
        binding.sendBtn.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Consent Edited Successfully", Toast.LENGTH_SHORT).show()
            finish()
        })
    }
}