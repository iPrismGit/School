package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.R
import com.iprism.school.databinding.ActivityEditRightsBinding
import com.iprism.school.databinding.ActivityEditStaffDetailsBinding
import com.iprism.school.utils.ToastUtils

class EditRightsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRightsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRightsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleSaveAndSubmitBtn()
    }

    private fun handleSaveAndSubmitBtn() {
        binding.saveAndSubmitBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Rights Edited Successfully!")
            finish()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}