package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCreateMealBinding

class CreateMealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateMealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}