package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityStudentDetailsBinding
import com.iprism.school.databinding.DeactiveStudentBottomSheetBinding
import com.iprism.school.databinding.DeleteBottomSheetBinding
import com.iprism.school.databinding.StudentItemBinding
import com.iprism.school.utils.ToastUtils

class StudentDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleDeActiveStudentIv()
    }

    private fun handleDeActiveStudentIv() {
        binding.deactiveIv.setOnClickListener(View.OnClickListener {
            showDeactivateBottomSheet()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showDeactivateBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = DeactiveStudentBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }

        binding.cancelBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        binding.crossIv.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        binding.deactivateBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
            ToastUtils.showSuccessCustomToast(this, "Student Deactivated Successfully!")
        })

        bottomSheetDialog.show()
    }
}