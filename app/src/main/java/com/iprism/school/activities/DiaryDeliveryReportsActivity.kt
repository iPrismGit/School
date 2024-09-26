package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iprism.school.R
import com.iprism.school.databinding.ActivityDiaryDeliveryReportsBinding

class DiaryDeliveryReportsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryDeliveryReportsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryDeliveryReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}