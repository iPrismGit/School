package com.iprism.school.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityStaffAttendanceEmailReportBinding
import com.iprism.school.model.Request.EmailReportReq
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class StaffAttendanceEmailReportActivity : BaseActivity() {

    private lateinit var binding: ActivityStaffAttendanceEmailReportBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffAttendanceEmailReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        handleBack()
        handleShareReportBtn()

        binding.fromDateLl.setOnClickListener(View.OnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(binding.root.context, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                binding.fromDateTxt.text = formattedDate
            }, year, month, day)

            // Set minimum date to today (only allow future dates)
            datePickerDialog.datePicker.minDate = calendar.timeInMillis

            datePickerDialog.show()
        })

        binding.toDateLl.setOnClickListener(View.OnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(binding.root.context, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                binding.toDateTxt.text = formattedDate
            }, year, month, day)

            // Set minimum date to today (only allow future dates)
            datePickerDialog.datePicker.minDate = calendar.timeInMillis

            datePickerDialog.show()
        })

        binding.shareReportBtn.setOnClickListener {

            if (binding.fromDateTxt.text.toString() == ""||binding.fromDateTxt.text.toString() == null){
                showToast("Select From Date")
            }else if(binding.fromDateTxt.text.toString() == ""||binding.fromDateTxt.text.toString() == null) {
                showToast("Select To Date")
            }else if (binding.emailTxt.text.toString() == ""||binding.emailTxt.text.toString() == null){
                showToast("Enter Email")
            }else{
                callstaffList()
            }

        }

    }

    private fun handleShareReportBtn() {
       binding.shareReportBtn.setOnClickListener(View.OnClickListener {
           ToastUtils.showSuccessCustomToast(this, "Report Emailed Successfully")
           finish()
       })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }


    private fun callstaffList() {
        showProgress()
        var apiRequest = EmailReportReq(auth_token,binding.emailTxt.text.toString()
            ,binding.fromDateTxt.text.toString(),scl_id,teacherId,binding.toDateTxt.text.toString())
        Log.d("staff_ListReq", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.mailReportStaff(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){
                        hideProgress()

                        val intent = Intent(this@StaffAttendanceEmailReportActivity, StaffAttendanceActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        hideProgress()
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@StaffAttendanceEmailReportActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@StaffAttendanceEmailReportActivity, t.message.toString())
            }
        })
    }

}