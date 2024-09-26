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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.adapters.CreatedDiariesAdapter
import com.iprism.school.databinding.ActivityCreatedDiaryBinding
import com.iprism.school.databinding.ActivityDaycareEmailReportBinding
import com.iprism.school.interfaces.OnCreatedDiariesClickListener
import com.iprism.school.utils.ToastUtils
import java.util.Locale

class CreatedDiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatedDiaryBinding
    private val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var calendar = Calendar.getInstance()
    private lateinit var crossImage: ImageView
    private lateinit var cancelBtn: Button
    private lateinit var deleteBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatedDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDate()
        handleLeftBtn()
        handleBack()
        setupCreatedDiariesAdapter()
    }

    private fun setupCreatedDiariesAdapter() {
        var createdDiariesAdapter = CreatedDiariesAdapter(this)
        binding.dairiesRv.adapter = createdDiariesAdapter
        var  linearLayoutManager = LinearLayoutManager(this)
        binding.dairiesRv.layoutManager = linearLayoutManager
        createdDiariesAdapter.setListener(object : OnCreatedDiariesClickListener{
                override fun onDeleteClickListener(dairyId: Int) {
                    showDeleteBottomSheet(dairyId)
                }

                override fun onInformationClickListener(dairyId: Int) {

                }

            }
        )
    }

    private fun showDeleteBottomSheet(diaryId : Int) {
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
            ToastUtils.showSuccessCustomToast(this, "Diary Deleted Successfully")
        })

        bottomSheetDialog.show()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
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

    private fun handleLeftBtn() {
        binding.leftArrowIv.setOnClickListener(View.OnClickListener {
            changeDate(-1)
        })
    }

    private fun changeDate(days: Int) {
        calendar.add(Calendar.DAY_OF_MONTH, days)
        binding.dateTxt.text = dateFormat.format(calendar.time)
        var dateFormatString = simpleDateFormat.format(calendar.time)
        Log.d("dateFormatString", dateFormatString)
    }

}