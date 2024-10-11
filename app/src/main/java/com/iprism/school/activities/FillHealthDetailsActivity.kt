package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityFillHealthDetailsBinding
import com.iprism.school.databinding.ActivityFillOtherDetailsBinding
import com.iprism.school.databinding.AddMoreBottomSheetLayoutBinding
import com.iprism.school.databinding.ChildHasHadBottomSheetBinding
import com.iprism.school.databinding.ChildSuffersFromBottomSheetBinding
import com.iprism.school.utils.ToastUtils

class FillHealthDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFillHealthDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillHealthDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBackBtn()
        handleAddStudentBtn()
        handleAddMoreBtn()
        handleChildHasHadLo()
        handleChildSuffersFromLo()
    }

    private fun handleChildHasHadLo() {
        binding.childHasHadLo.setOnClickListener(View.OnClickListener {
            showChildHasHadBottomSheet()
        })
    }

    private fun handleChildSuffersFromLo() {
        binding.childSuffersFromLo.setOnClickListener(View.OnClickListener {
            showChildSuffersFromBottomSheet()
        })
    }

    private fun handleAddMoreBtn() {
        binding.addBtn.setOnClickListener(View.OnClickListener {
            showAddMoreBottomSheet()
        })
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


    private fun showChildHasHadBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = ChildHasHadBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            binding.okBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "More Added Successfully")
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

    private fun showChildSuffersFromBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = ChildSuffersFromBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            binding.okBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "More Added Successfully")
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

    private fun showAddMoreBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = AddMoreBottomSheetLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            binding.okBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "More Added Successfully")
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