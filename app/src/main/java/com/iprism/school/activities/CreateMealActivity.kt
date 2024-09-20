package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityCreateMealBinding

class CreateMealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateMealBinding
    private lateinit var crossImage: ImageView
    private lateinit var okBtn: Button
    private lateinit var cancelBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleSubmitBtn()
        handleFoodTypeLo()
    }

    private fun handleFoodTypeLo() {
        binding.foodTypeLo.setOnClickListener(View.OnClickListener {
            showMealTypeBottomSheet()
        })
    }

    private fun handleSubmitBtn() {
        binding.submitBtn.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Meal Created Successfully", Toast.LENGTH_SHORT).show()
            finish()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showMealTypeBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View =
            LayoutInflater.from(this).inflate(R.layout.meal_type_bottom_sheet_dialog, null)
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
            Toast.makeText(this, "Meal Added", Toast.LENGTH_SHORT).show()
        })

        bottomSheetDialog.show()
    }

}