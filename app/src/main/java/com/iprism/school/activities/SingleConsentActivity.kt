package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iprism.school.R
import com.iprism.school.databinding.ActivitySingleConsentBinding
import com.iprism.school.databinding.ConsentItemBinding

class SingleConsentActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleConsentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleConsentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}