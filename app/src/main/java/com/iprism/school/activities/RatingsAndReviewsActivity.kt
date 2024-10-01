package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iprism.school.R
import com.iprism.school.databinding.ActivityRatingsAndReviewsBinding

class RatingsAndReviewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRatingsAndReviewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingsAndReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}