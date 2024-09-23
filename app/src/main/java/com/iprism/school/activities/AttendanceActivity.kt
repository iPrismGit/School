package com.iprism.school.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.adapters.StudentsAttendanceAdapter
import com.iprism.school.databinding.ActivityAttendanceBinding
import com.iprism.school.databinding.ActivityStaffAttendanceBinding
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils

class AttendanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAttendanceBinding
    private var attendanceType: String = "pending"
    private var selectedDate = ""
    private lateinit var crossImage: ImageView
    private lateinit var attendanceCrossImage: ImageView
    private lateinit var cancelBtn: Button
    private lateinit var applyBtn: Button
    private lateinit var markBtn: Button
    private lateinit var attendanceCancelBtn: Button
    private lateinit var dateLo: ConstraintLayout
    private lateinit var dateTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        setupSudentsAttendanceAdapter()
        handlePendingLo()
        handleRejectedLo()
        handleEditBtn()
        handleSaveAttendanceBtn()
    }

    private fun setupSudentsAttendanceAdapter() {
        var attendanceAdapter = StudentsAttendanceAdapter(this)
        binding.studentAttendanceRv.adapter = attendanceAdapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.studentAttendanceRv.layoutManager = linearLayoutManager
    }

    private fun handleSaveAttendanceBtn() {
        binding.saveAttendanceBtn.setOnClickListener(View.OnClickListener {
            showAttendanceConformationBottomSheet()
        })
    }

    private fun handleEditBtn() {
        binding.editIv.setOnClickListener(View.OnClickListener {
            showClassesBottomSheet()
        })
    }

    private fun handleRejectedLo() {
        binding.rejectedLo.setOnClickListener(View.OnClickListener {
            binding.rejectedTxt.setTextColor(resources.getColor(R.color.blue3))
            binding.rejectedCountTxt.setTextColor(resources.getColor(R.color.blue3))
            binding.pendingTxt.setTextColor(resources.getColor(R.color.gray1))
            binding.pendingCountTxt.setTextColor(resources.getColor(R.color.gray1))
            attendanceType = "rejected"
        })
    }

    private fun handlePendingLo() {
        binding.pendingLo.setOnClickListener(View.OnClickListener {
            binding.pendingTxt.setTextColor(resources.getColor(R.color.blue3))
            binding.pendingCountTxt.setTextColor(resources.getColor(R.color.blue3))
            binding.rejectedTxt.setTextColor(resources.getColor(R.color.gray1))
            binding.rejectedCountTxt.setTextColor(resources.getColor(R.color.gray1))
            attendanceType = "pending"
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun showAttendanceConformationBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View =
            LayoutInflater.from(this).inflate(R.layout.all_students_present_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        attendanceCancelBtn = bottomSheetDialog.findViewById<View>(R.id.cancel_btn) as Button
        attendanceCrossImage = bottomSheetDialog.findViewById<View>(R.id.cross_iv) as ImageView
        markBtn = bottomSheetDialog.findViewById<View>(R.id.mark_button) as Button
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }

        attendanceCancelBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        attendanceCrossImage.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        markBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
            ToastUtils.showSuccessCustomToast(this, "Attendance Marked Successfully")
        })

        bottomSheetDialog.show()
    }

    private fun showClassesBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View =
            LayoutInflater.from(this).inflate(R.layout.switch_user_bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        cancelBtn = bottomSheetDialog.findViewById<View>(R.id.cancel_btn) as Button
        crossImage = bottomSheetDialog.findViewById<View>(R.id.cross_iv) as ImageView
        applyBtn = bottomSheetDialog.findViewById<View>(R.id.apply_button) as Button
        dateLo = bottomSheetDialog.findViewById<View>(R.id.date_lo) as ConstraintLayout
        dateTxt = bottomSheetDialog.findViewById<View>(R.id.date_txt) as TextView
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }

        dateLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(dateTxt, false)

        })

        cancelBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        crossImage.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        applyBtn.setOnClickListener(View.OnClickListener {
            selectedDate = dateTxt.text.toString()
            bottomSheetDialog.dismiss()
            binding.dateTxt.text = selectedDate
            Log.d("SelectedDate", selectedDate)
        })

        bottomSheetDialog.show()
    }

}