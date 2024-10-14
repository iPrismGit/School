package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCreateGroupBinding
import com.iprism.school.databinding.ActivityGroupsBinding
import com.iprism.school.databinding.AddMoreBottomSheetLayoutBinding
import com.iprism.school.databinding.GroupAdminsBottomSheetBinding
import com.iprism.school.databinding.SchoolMembersBottomSheetBinding
import com.iprism.school.databinding.StudentsBottomSheetBinding
import com.iprism.school.utils.ToastUtils

class CreateGroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleAdminsLo()
        handleSchoolMembersLo()
        handleStudentsLo()
        handleCreateBtn()
    }

    private fun handleCreateBtn() {
        binding.createBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Group Created Successfully!")
            finish()
        })
    }

    private fun handleStudentsLo() {
        binding.studentsLo.setOnClickListener(View.OnClickListener {
            showStudentsBottomSheet()
        })
    }

    private fun handleSchoolMembersLo() {
        binding.schoolMembersLo.setOnClickListener(View.OnClickListener {
            showSchoolMembersBottomSheet()
        })
    }

    private fun handleAdminsLo() {
        binding.adminsLo.setOnClickListener(View.OnClickListener {
            showGroupAdminsBottomSheet()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showGroupAdminsBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = GroupAdminsBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            binding.okBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            binding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            binding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

    private fun showStudentsBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = StudentsBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            binding.okBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            binding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            binding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

    private fun showSchoolMembersBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = SchoolMembersBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            binding.okBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            binding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            binding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

}