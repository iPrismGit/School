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
import com.iprism.school.databinding.ActivityCreateCalenderBinding
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateCalenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateCalenderBinding
    private lateinit var crossImage: ImageView
    private lateinit var okBtn: Button
    private lateinit var cancelBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDateAndTime()
        handleDateLo()
        handleTimeLo()
        handleBack()
        handleAdvanceOptionsLo()
        handleLinkIv()
        handleLocationIv()
        handleCreateBtn()
    }

    private fun handleCreateBtn() {
        binding.createBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Event created Successfully")
            finish()
        })
    }

    private fun handleTimeLo() {
        binding.timeLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getTime(binding.timeTxt)
        })
    }

    private fun handleDateLo() {
        binding.dateLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(binding.dateTxt, false)
        })
    }

    private fun setDateAndTime() {
        val calendar: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val stf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedDate: String = sdf.format(calendar.time)
        val formattedTime = stf.format(calendar.time)
        binding.dateTxt.text = formattedDate
        binding.timeTxt.text = formattedTime
    }

    private fun handleLinkIv() {
        binding.linkIv.setOnClickListener(View.OnClickListener {
            showOptionsDialog()
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

    private fun handleLocationIv() {
        binding.addLocationIv.setOnClickListener(View.OnClickListener {
            binding.locationTxt.visibility = View.VISIBLE
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleAdvanceOptionsLo() {
        binding.advancedOptionsLo.setOnClickListener(View.OnClickListener {
            showAdvancedOptionsBottomSheet()
        })
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