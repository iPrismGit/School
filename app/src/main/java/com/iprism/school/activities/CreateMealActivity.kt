package com.iprism.school.activities

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
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
import com.iprism.school.databinding.ActivityCreateMealBinding
import com.iprism.school.utils.ToastUtils
import java.util.Locale

class CreateMealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateMealBinding
    private val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var calendar = Calendar.getInstance()
    private lateinit var crossImage: ImageView
    private lateinit var okBtn: Button
    private lateinit var cancelBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDate()
        handleBack()
        handleSubmitBtn()
        handleFoodTypeLo()
        hanldeLeftBtn()
        handleRightBtn()
    }

    private fun setDate() {
        val calendar: java.util.Calendar = java.util.Calendar.getInstance()
        val sdf = java.text.SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val sdfString = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = sdf.format(calendar.time)
        val formattedDateString: String = sdfString.format(calendar.time)
        Log.d("dateFormatString", formattedDateString)
        binding.dateTxt.text = formattedDate
    }

    private fun hanldeLeftBtn() {
        binding.leftArrowIv.setOnClickListener(View.OnClickListener {
            changeDate(-1)
        })
    }

    private fun handleRightBtn() {
        binding.rightArrowIv.setOnClickListener(View.OnClickListener {
            changeDate(1)
        })
    }

    private fun changeDate(days: Int) {
        calendar.add(Calendar.DAY_OF_MONTH, days)
        binding.dateTxt.text = dateFormat.format(calendar.time)
        var dateFormatString = simpleDateFormat.format(calendar.time)
        Log.d("dateFormatString", dateFormatString)
    }

    private fun handleFoodTypeLo() {
        binding.foodTypeLo.setOnClickListener(View.OnClickListener {
            showMealTypeBottomSheet()
        })
    }

    private fun handleSubmitBtn() {
        binding.submitBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Meal Created Successfully")
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
        })

        bottomSheetDialog.show()
    }

}