package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCalenderDetailsBinding
import com.iprism.school.utils.ToastUtils

class CalenderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalenderDetailsBinding
    private var calenderId : String = ""
    private var isInfoVisible: Boolean = false
    private lateinit var crossImage: ImageView
    private lateinit var cancelBtn: Button
    private lateinit var deleteBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalenderDetailsBinding.inflate(layoutInflater)
        calenderId = intent.getStringExtra("calenderId").toString()
        Log.d("CalenderId", calenderId)
        setContentView(binding.root)
        handleBack()
        handleEdit()
        handleDelete()
        handleDownArrow()
    }

    private fun handleDownArrow() {
        binding.downArrow.setOnClickListener(View.OnClickListener {
            toggleInformationVisibility()
        })
    }

    private fun toggleInformationVisibility() {
        if (isInfoVisible) {
            binding.detailsLo.visibility = View.GONE
            binding.downArrow.setImageResource(R.drawable.down_arrow_img)
        } else {
            binding.detailsLo.visibility = View.VISIBLE
            binding.downArrow.setImageResource(R.drawable.up_arrow_img)
        }
        isInfoVisible = !isInfoVisible
    }

    private fun handleDelete() {
        binding.deleteButton.setOnClickListener(View.OnClickListener {
            showDeleteBottomSheet()
        })
    }

    private fun handleEdit() {
        binding.editIv.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, EditCalenderActivity::class.java)
            intent.putExtra("calenderId", calenderId)
            startActivity(intent)
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }


    private fun showDeleteBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View = LayoutInflater.from(this).inflate(R.layout.delete_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        cancelBtn = bottomSheetDialog.findViewById<View>(R.id.cancel_btn) as Button
        crossImage = bottomSheetDialog.findViewById<View>(R.id.cross_iv) as ImageView
        deleteBtn = bottomSheetDialog.findViewById<View>(R.id.delete_button) as Button
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

        deleteBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
            ToastUtils.showSuccessCustomToast(this, "Event Deleted Successfully")
            finish()
        })

        bottomSheetDialog.show()
    }
}