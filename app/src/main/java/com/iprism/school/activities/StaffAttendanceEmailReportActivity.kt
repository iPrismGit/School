package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.R
import com.iprism.school.databinding.ActivityStaffAttendanceBinding
import com.iprism.school.databinding.ActivityStaffAttendanceEmailReportBinding
import com.iprism.school.utils.ToastUtils

class StaffAttendanceEmailReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStaffAttendanceEmailReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffAttendanceEmailReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        handleShareReportBtn()
    }

    private fun handleShareReportBtn() {
       binding.shareReportBtn.setOnClickListener(View.OnClickListener {
           ToastUtils.showCustomToast(this, "Report Emailed Successfully")
           finish()
       })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}