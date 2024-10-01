package com.iprism.school.activities

import android.annotation.SuppressLint
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.iprism.school.R
import com.iprism.school.databinding.ActivityPromotionsBinding

class PromotionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPromotionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromotionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.promotionsImg.borderWidth = 4
        binding.promotionsImg.borderColor = ContextCompat.getColor(this, R.color.blue)
    }

}