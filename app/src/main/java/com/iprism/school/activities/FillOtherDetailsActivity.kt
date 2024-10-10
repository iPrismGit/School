package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityFillOtherDetailsBinding
import com.iprism.school.databinding.ActivityFillParentDetailsBinding
import com.iprism.school.databinding.ActivityFillSchoolDetailsBinding
import com.iprism.school.utils.ToastUtils

class FillOtherDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFillOtherDetailsBinding
    private lateinit var crossImage: ImageView
    private lateinit var okBtn: Button
    private lateinit var cancelBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillOtherDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleAddBtn()
        handleNextBtn()
    }

    private fun handleNextBtn() {
        binding.nextButton.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, FillHealthDetailsActivity::class.java)
            startActivity(intent)
        })
    }

    private fun handleAddBtn() {
        binding.addBtn.setOnClickListener(View.OnClickListener {
            showAuthorizedPersonBottomSheet()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showAuthorizedPersonBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View =
            LayoutInflater.from(this).inflate(R.layout.authorized_person_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        cancelBtn = bottomSheetDialog.findViewById<View>(R.id.cancel_btn) as Button
        crossImage = bottomSheetDialog.findViewById<View>(R.id.cross_iv) as ImageView
        okBtn = bottomSheetDialog.findViewById<View>(R.id.ok_button) as Button
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }

        cancelBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        crossImage.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        okBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Authorized Person Added Successfully...")
            bottomSheetDialog.dismiss()
        })

        bottomSheetDialog.show()
    }
}