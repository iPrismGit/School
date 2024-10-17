package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityClassSubjectsBinding
import com.iprism.school.databinding.ActivityCreateClassSubjectsBinding
import com.iprism.school.databinding.AddMoreBottomSheetLayoutBinding
import com.iprism.school.databinding.SchoolMembersBottomSheetBinding
import com.iprism.school.databinding.StudentsBottomSheetBinding
import com.iprism.school.databinding.SubjectsBottomSheetBinding
import com.iprism.school.utils.ToastUtils

class CreateClassSubjectsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateClassSubjectsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateClassSubjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleSaveBtn()
        handleSubjectsLo()
        handleTeachersLo()
    }

    private fun handleTeachersLo() {
        binding.teachersLo.setOnClickListener(View.OnClickListener {
            showTeachersBottomSheet()
        })
    }

    private fun handleSubjectsLo() {
        binding.subjectLo.setOnClickListener(View.OnClickListener {
            showSubjectsBottomSheet()
        })
    }

    private fun handleSaveBtn() {
        binding.saveBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Class Subject Added Successfully!")
            finish()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showSubjectsBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val studentsBinding = SubjectsBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(studentsBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            studentsBinding.okBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "More Added Successfully")
            })

            studentsBinding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            studentsBinding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
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
                ToastUtils.showSuccessCustomToast(this, "More Added Successfully")
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