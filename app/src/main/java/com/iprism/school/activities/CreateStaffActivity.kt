package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCreateStaffBinding
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils

class CreateStaffActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateStaffBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleCreateUserBtn()
        handleBack()
        handleDOBCalenderLo()
        handleDOJCalenderLo()
    }

    private fun handleDOJCalenderLo() {
        binding.dateOfJoiningCvLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(binding.dateOfJoiningTxt, false)
        })
    }

    private fun handleDOBCalenderLo() {
        binding.dateOfBirthCvLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(binding.dateOfBirthTxt, true)
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleCreateUserBtn() {
        binding.createUserBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Created USer Successfully!")
            finish()
        })
    }
}