package com.iprism.school.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.databinding.ActivityDiaryDetailsBinding
import com.iprism.school.utils.Constants

class DiaryDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryDetailsBinding
    private var studentId = ""
    private var image = ""
    private var type = ""
    private var details = ""
    private var firstName = ""
    private var middleName = ""
    private var lastName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDiaryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        studentId = intent.getStringExtra("studentId").toString()
        image = intent.getStringExtra("image").toString()
        type = intent.getStringExtra("type").toString()
        details = intent.getStringExtra("details").toString()
        firstName = intent.getStringExtra("firstName").toString()
        middleName = intent.getStringExtra("middleName").toString()
        lastName = intent.getStringExtra("lastName").toString()
        handelBack()
        setupData()
    }

    private fun setupData() {
        if (studentId.equals("all", true)){
            binding.sentToTxt.text = "Sent to All Students"
        } else{
            binding.sentToTxt.text = "Sent to " + firstName + " " + middleName + " " + lastName
        }
        if (type.equals("cw", true)){
            binding.diaryTypeTxt.text = "Diary Type : Class Work"
        } else{
            binding.diaryTypeTxt.text = "Diary Type :Home Work"
        }
        binding.detailsTxt.text = "Details : " + details
        Log.d("Image", image)
        if (image.equals("", true)){
            binding.noImgTxt.visibility = View.VISIBLE
            binding.diaryImg.visibility = View.GONE
        }
        Glide.with(this).load(Constants.IMAGES_URL+image).error(ContextCompat.getDrawable(this,R.drawable.dummy_logo)).into(binding.diaryImg)

    }

    private fun handelBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

}