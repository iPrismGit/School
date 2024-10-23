package com.iprism.school.activities

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityAlbumDetailsBinding
import com.iprism.school.databinding.ActivityChildHandOverBinding
import com.iprism.school.databinding.ChildHandoverConfirmationBottomSheetBinding
import com.iprism.school.databinding.DeleteBottomSheetBinding
import com.iprism.school.utils.ToastUtils

class ChildHandOverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChildHandOverBinding
    private lateinit var childImageBItmap :Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChildHandOverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        childImageBItmap = intent.getParcelableExtra<Bitmap>("capturedImage")!!
        if (childImageBItmap != null){
            binding.childImageIv.setImageBitmap(childImageBItmap)
        }
        handleBack()
        handleResetBtn()
        handleHandoverBtn()
    }

    private fun handleHandoverBtn() {
        binding.handoverBtn.setOnClickListener(View.OnClickListener {
            showConfirmationBottomSheet()
        })
    }

    private fun handleResetBtn() {
        binding.resetBtn.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showConfirmationBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val handoverConfirmationBinding = ChildHandoverConfirmationBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(handoverConfirmationBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }
        handoverConfirmationBinding.noBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        handoverConfirmationBinding.crossIv.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        handoverConfirmationBinding.yesBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
            ToastUtils.showSuccessCustomToast(this, "Child Handover Done Successfully")
            finish()
        })

        bottomSheetDialog.show()
    }
}