package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.R
import com.iprism.school.databinding.ActivityEditStaffDetailsBinding
import com.iprism.school.utils.ToastUtils

class EditStaffDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditStaffDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityEditStaffDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleUpdatesBtn()
        handleEditRightsBtn()
    }

    private fun handleEditRightsBtn() {
        binding.editRightsBtn.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, EditRightsActivity::class.java)
            startActivity(intent)
        })
    }

    private fun handleUpdatesBtn() {
        binding.updatesBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Updated Successfully!")
            finish()
        })
    }


    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}