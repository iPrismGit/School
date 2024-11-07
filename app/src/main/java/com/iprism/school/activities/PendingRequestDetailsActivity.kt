package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iprism.school.R
import com.iprism.school.databinding.ActivityPendingRequestDetailsBinding
import com.iprism.school.databinding.PendingRequestItemBinding

class PendingRequestDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPendingRequestDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingRequestDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}