package com.iprism.school.activities

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivitySingleConsentBinding

class SingleConsentActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleConsentBinding
    private var isInfoVisible: Boolean = false
    private lateinit var crossImage: ImageView
    private lateinit var cancelImage: ImageView
    private lateinit var deleteImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleConsentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleDownArrow()
        handleDeleteBtn()
        handleEditBtn()
        handleInfoBtn()
        handleEmailBtn()
    }

    private fun handleEmailBtn() {
        binding.emailIv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ConsentEmailReportActivity::class.java)
            startActivity(intent)
        })
    }

    private fun handleInfoBtn() {
        binding.infoIv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ConsentInfoActivity::class.java)
            startActivity(intent)
        })
    }

    private fun handleEditBtn() {
        binding.editIv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, EditConsentActivity::class.java)
            startActivity(intent)
        })
    }

    private fun handleDeleteBtn() {
        binding.trashIv.setOnClickListener(View.OnClickListener {
            showDeleteBottomSheet()
        })
    }

    private fun handleDownArrow() {
        binding.dropDownIv.setOnClickListener(View.OnClickListener {
            toggleInformationVisibility()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun toggleInformationVisibility() {
        if (isInfoVisible) {
            binding.infoLo.visibility = View.GONE
            binding.dropDownIv.setImageResource(R.drawable.down_arrow_img)
        } else {
            binding.infoLo.visibility = View.VISIBLE
            binding.dropDownIv.setImageResource(R.drawable.up_arrow_img)
        }
        isInfoVisible = !isInfoVisible
    }

    private fun showDeleteBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View = LayoutInflater.from(this).inflate(R.layout.delete_consent_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        cancelImage = bottomSheetDialog.findViewById<View>(R.id.cancel_button) as ImageView
        crossImage = bottomSheetDialog.findViewById<View>(R.id.cross_iv) as ImageView
        deleteImage = bottomSheetDialog.findViewById<View>(R.id.delete_button) as ImageView
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }

        cancelImage.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        crossImage.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        deleteImage.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
        })

        bottomSheetDialog.show()
    }

}