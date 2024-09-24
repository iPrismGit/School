package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCalenderBinding
import com.iprism.school.databinding.ActivityEditCalenderBinding
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils

class EditCalenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditCalenderBinding
    private lateinit var crossImage: ImageView
    private var calenderId: String = ""
    private lateinit var okBtn: Button
    private lateinit var cancelBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        calenderId = intent.getStringExtra("calenderId").toString()
        handleBack()
        handleLink()
        handleLocationIv()
        handleAdavancedOptionsLo()
        handleUpdateBtn()
        handleDateLo()
        handleTimeLo()
    }

    private fun handleTimeLo() {
        binding.timeTxt.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getTime(binding.timeTxt)
        })
    }

    private fun handleDateLo() {
        binding.dateLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(binding.dateTxt, false)
        })
    }

    private fun handleUpdateBtn() {
        binding.updateBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Event Updated Successfully")
            finish()
        })
    }

    private fun handleAdavancedOptionsLo() {
        binding.advancedOptionsLo.setOnClickListener(View.OnClickListener {
            showAdvancedOptionsBottomSheet()
        })
    }

    private fun handleLocationIv() {
        binding.addLocationIv.setOnClickListener(View.OnClickListener {
            binding.locationTxt.visibility = View.VISIBLE
        })
    }

    private fun handleLink() {
        binding.linkIv.setOnClickListener(View.OnClickListener {
            showOptionsDialog()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showOptionsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.camera_dilog_box, null)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialog.show()
    }


    private fun showAdvancedOptionsBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View =
            LayoutInflater.from(this).inflate(R.layout.advanced_options_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        cancelBtn = bottomSheetDialog.findViewById<View>(R.id.cancel_btn) as Button
        crossImage = bottomSheetDialog.findViewById<View>(R.id.cross_iv) as ImageView
        okBtn = bottomSheetDialog.findViewById<View>(R.id.ok_btn) as Button
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
            bottomSheetDialog.dismiss()
        })
        bottomSheetDialog.show()
    }
}