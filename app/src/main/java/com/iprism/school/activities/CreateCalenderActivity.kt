package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCreateCalenderBinding
import com.iprism.school.databinding.CalenderItemBinding

class CreateCalenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateCalenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}