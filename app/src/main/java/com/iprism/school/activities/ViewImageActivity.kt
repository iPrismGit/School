package com.iprism.school.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.ActivityViewImageBinding
import com.iprism.school.utils.Constants

class ViewImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewImageBinding
    private var eventImage = ""
    private var eventName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        eventImage = intent.getStringExtra("EventImage").toString()
        eventName = intent.getStringExtra("EventName").toString()
        ViewCompat.setOnApplyWindowInsetsListener( binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        binding.titleTxt.text = eventName
        Glide.with(this).load(Constants.IMAGES_URL + eventImage).error(ContextCompat.getDrawable(this, R.drawable.dummy_logo)).into(binding.imageIv)
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

}