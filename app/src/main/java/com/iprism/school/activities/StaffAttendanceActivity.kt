package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.adapters.StaffAttendancesAdapter
import com.iprism.school.databinding.ActivityStaffAttendanceBinding
import com.iprism.school.model.Request.StaffAttandanceReq
import com.iprism.school.model.Response.StaffAttandanceResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StaffAttendanceActivity : BaseActivity() {

    private lateinit var binding: ActivityStaffAttendanceBinding
    private var selectedType: String = "marked"

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var selectedDate: String = ""

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH) // e.g., "03 April 2025"

    private val apiFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        handleEmailIv()
        setupStaffAttendanceAdapter()
        setupRadioButtonListener(binding.staffRadioGroup)

        binding.backIv.setOnClickListener {
            val intent = Intent(this@StaffAttendanceActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        updateDate()

       binding.leftArrowIv.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, -1) // Move to previous day
            updateDate()
        }

        binding.rightArrowIv.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, 1) // Move to next day
            updateDate()
        }

    }

    private fun handleEmailIv() {
        binding.emailIv.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, StaffAttendanceEmailReportActivity::class.java))
        })
    }

    private fun setupStaffAttendanceAdapter() {
//        var staffAttendancesAdapter = StaffAttendancesAdapter(this)
//        binding.staffAttendanceRv.adapter = staffAttendancesAdapter
//        var layoutManager = LinearLayoutManager(this)
//        binding.staffAttendanceRv.layoutManager = layoutManager
    }

    private fun setupRadioButtonListener(radioGroup: RadioGroup) {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.marked_staff_rb -> {
                    selectedType = "marked"
                    callstaffList()
//                    Toast.makeText(this, "Selected: $selectedType", Toast.LENGTH_SHORT).show()
                }

                R.id.not_marked_staff_rb -> {
                    selectedType = "not_marked"
                    callstaffList()
//                    Toast.makeText(this, "Selected: $selectedType", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun callstaffList() {
        showProgress()
        var apiRequest = StaffAttandanceReq(selectedType,auth_token,selectedDate.toString(),scl_id.toString(),teacherId.toString())
        Log.d("staff_ListReq", apiRequest.toString())
        val call: Call<StaffAttandanceResponse> = parentApiService!!.viewStaffAttandance(apiRequest)
        call.enqueue(object : Callback<StaffAttandanceResponse> {
            override fun onResponse(call: Call<StaffAttandanceResponse>, response: Response<StaffAttandanceResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){
                        hideProgress()
                        binding.nodata.visibility = View.GONE
                        binding.staffAttendanceRv.visibility = View.VISIBLE

                        val adap1 = StaffAttendancesAdapter(this@StaffAttendanceActivity, loginApiResponse.response.attendance)
                        binding.staffAttendanceRv.layoutManager = LinearLayoutManager(this@StaffAttendanceActivity, LinearLayoutManager.VERTICAL, false)
                        binding.staffAttendanceRv.adapter = adap1
                        adap1.notifyDataSetChanged()
                    }else{
                        hideProgress()
                        binding.nodata.visibility = View.VISIBLE
                        binding.staffAttendanceRv.visibility = View.GONE
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@StaffAttendanceActivity, response.message())
                }
            }
            override fun onFailure(call: Call<StaffAttandanceResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@StaffAttendanceActivity, t.message.toString())
            }
        })
    }

    private fun updateDate() {
        binding.dateTxt.text = dateFormat.format(calendar.time)
        selectedDate= apiFormat.format(calendar.time)
        callstaffList()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@StaffAttendanceActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()

    }

}