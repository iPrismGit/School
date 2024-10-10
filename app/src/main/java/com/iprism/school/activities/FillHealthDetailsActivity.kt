package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.R
import com.iprism.school.databinding.ActivityFillHealthDetailsBinding
import com.iprism.school.databinding.ActivityFillOtherDetailsBinding
import com.iprism.school.utils.ToastUtils

class FillHealthDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFillHealthDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillHealthDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBackBtn()
        handleAddStudentBtn()
    }

    private fun handleAddStudentBtn() {
        binding.addStudentButton.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Student Added Successfully")
        })
    }

    private fun handleBackBtn() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }


}