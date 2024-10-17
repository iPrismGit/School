package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCreateStaffBinding
import com.iprism.school.databinding.AddMoreBottomSheetLayoutBinding
import com.iprism.school.databinding.GenderBottomSheetDialogBinding
import com.iprism.school.databinding.RightsBottomSheetBinding
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils

class CreateStaffActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateStaffBinding
    private var genderType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleCreateUserBtn()
        handleBack()
        handleDOBCalenderLo()
        handleDOJCalenderLo()
        handleGenderLo()
        handleRightsLo()
    }

    private fun handleRightsLo() {
        binding.accessRightsLo.setOnClickListener(View.OnClickListener {
            showAddRightsBottomSheet()
        })
    }

    private fun handleGenderLo() {
        binding.genderLo.setOnClickListener(View.OnClickListener {
            showGenderBottomSheet()
        })
    }

    private fun showGenderBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomBinding = GenderBottomSheetDialogBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            setupCheckboxes(bottomBinding.maleCb, bottomBinding.femaleCb)
            bottomBinding.confirmBtn.setOnClickListener(View.OnClickListener {
                if (genderType.equals("")) {
                    ToastUtils.showErrorCustomToast(this, "Please Select gender Type")
                } else {
                    bottomSheetDialog.dismiss()
                    binding.genderTxt.text = genderType
                    ToastUtils.showSuccessCustomToast(this, genderType)
                }
            })

            bottomBinding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            bottomBinding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

    private fun showAddRightsBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val rightsBinding = RightsBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(rightsBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            rightsBinding.confirmBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "Rights Added Successfully")
            })

            rightsBinding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            rightsBinding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
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

    private fun setupCheckboxes(checkBox1: CheckBox, checkBox2: CheckBox) {
        checkBox1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBox2.isChecked = false
                genderType = "Male"
            } else if (!checkBox2.isChecked) {
                genderType = ""
            }
        }

        checkBox2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBox1.isChecked = false
                genderType = "Female"
            } else if (!checkBox1.isChecked) {
                genderType = ""
            }
        }
    }

}