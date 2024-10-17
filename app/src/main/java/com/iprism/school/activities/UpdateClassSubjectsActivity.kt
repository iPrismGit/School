package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCreateClassSubjectsBinding
import com.iprism.school.databinding.ActivityUpDateClassSubjectsBinding
import com.iprism.school.databinding.SchoolMembersBottomSheetBinding
import com.iprism.school.utils.ToastUtils

class UpdateClassSubjectsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpDateClassSubjectsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpDateClassSubjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleSave()
        handleTeachersLo()
    }

    private fun handleTeachersLo() {
        binding.teachersLo.setOnClickListener(View.OnClickListener {
            binding.teachersLo.setOnClickListener(View.OnClickListener {
                showTeachersBottomSheet()
            })
        })
    }

    private fun handleSave() {
        binding.saveBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Subject Updated Successfully!")
            finish()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showTeachersBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val teachersBinding = SchoolMembersBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(teachersBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            teachersBinding.okBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "Added Teachers")
            })

            teachersBinding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            teachersBinding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

}