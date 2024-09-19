package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCreateMealBinding

class CreateMealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateMealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleSubmitBtn()
    }

    private fun handleSubmitBtn() {
        binding.submitBtn.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Meal Created Successfully", Toast.LENGTH_SHORT).show()
            finish()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}