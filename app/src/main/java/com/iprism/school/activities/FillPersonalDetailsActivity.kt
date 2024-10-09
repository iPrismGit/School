package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iprism.school.R
import com.iprism.school.databinding.ActivityFillPersonalDetailsBinding

class FillPersonalDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFillPersonalDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillPersonalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}