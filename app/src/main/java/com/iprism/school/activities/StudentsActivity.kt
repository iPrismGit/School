package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.R
import com.iprism.school.databinding.ActivityStudentsBinding

class StudentsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStudentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleStudent()
    }

    private fun handleStudent() {
        binding.linearLayout4.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, StudentDetailsActivity::class.java))
        })
    }
}