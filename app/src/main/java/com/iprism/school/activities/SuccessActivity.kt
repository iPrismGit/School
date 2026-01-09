package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iprism.school.databinding.ActivitySuccessBinding
import kotlin.jvm.java
import kotlin.toString

class SuccessActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuccessBinding
    private var tag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.successImg.scaleX = 0f
        binding.successImg.scaleY = 0f
        binding.successImg.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(600)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
        binding.mesageTxt.alpha = 0f
        binding.mesageTxt.translationY = 50f
        binding.mesageTxt.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(300)
            .setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        tag = intent.getStringExtra("tag").toString()
        binding.mesageTxt.text = "$tag Successfully!"
        Handler().postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 2000)
    }
}