package com.iprism.school.activities.calender

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.activities.HomeActivity
import com.iprism.school.activities.LoginActivity
import com.iprism.school.adapters.CalenderAdapter
import com.iprism.school.databinding.ActivityCalenderBinding
import com.iprism.school.model.Request.TeacherCalenderlistReq
import com.iprism.school.utils.User
import com.iprism.school.viewModels.Scl_ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalenderActivity : BaseActivity() {

    private lateinit var binding: ActivityCalenderBinding

    private val viewModel: Scl_ViewModel by viewModels()
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var formattedDateString: String = ""

    private val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var calendar = Calendar.getInstance()
    private lateinit var adapter: CalenderAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    private var displayedDate = LocalDate.now() // e.g., 2025-02-23
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        setDate()
        handleBack()
        handleAddCalenderBtn()
        handleRightBtn()
        hanldeLeftBtn()
        updateMonthYearText()
    }

    private fun setDate() {
        val calendar: java.util.Calendar = java.util.Calendar.getInstance()
        val sdf = java.text.SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val sdfString = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = sdf.format(calendar.time)
         formattedDateString  = sdfString.format(calendar.time)
        Log.d("dateFormatString", formattedDateString)
        binding.dateTxt.text = formattedDate
        calenderList(formattedDateString)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun hanldeLeftBtn() {
        binding.leftArrowIv.setOnClickListener(View.OnClickListener {
            changeDate(-1)
            displayedDate = displayedDate.minusMonths(1)
            updateMonthYearText()
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleRightBtn() {
        binding.rightArrowIv.setOnClickListener(View.OnClickListener {
            changeDate(1)
            displayedDate = displayedDate.plusMonths(1)
            updateMonthYearText()
        })
    }

    private fun changeDate(days: Int) {
        calendar.add(Calendar.MONDAY, days)
        var dateFormatString = simpleDateFormat.format(calendar.time)
        Log.d("dateFormatString", dateFormatString)
        calenderList(dateFormatString)
    }


    private fun handleAddCalenderBtn() {
        binding.addCalenderBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@CalenderActivity, CreateCalenderActivity::class.java)
            intent.putExtra("tag","create")
            startActivity(intent)
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@CalenderActivity,HomeActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    private fun calenderList(formattedDateString: String) {
        showProgress()
        var apiRequest = TeacherCalenderlistReq(auth_token,formattedDateString,scl_id,teacherId)
        Log.d("calenderListReq", apiRequest.toString())
        viewModel.teacherCalenderList(apiRequest).observe(this@CalenderActivity, Observer { response ->

            hideProgress()

            if (response != null && response.status == true) {
                hideProgress()
                Log.d("calenderListResponse", response.toString())

                binding.nodataTv.visibility = View.GONE
                binding.calendersRv.visibility = View.VISIBLE




                adapter = CalenderAdapter(this,response.response.calender_details ?: emptyList())
                binding.calendersRv.adapter = adapter
                var layoutManager = LinearLayoutManager(this)
                binding.calendersRv.layoutManager = layoutManager

                adapter.OnItemCallPic = {
                        mydata ->
                    val calenderId = mydata.id.toString()
                    val intent = Intent(this@CalenderActivity, CalenderDetailsActivity::class.java)
                    intent.putExtra("calenderId",calenderId)
                    startActivity(intent)
                }

            } else {
                hideProgress()
                binding.nodataTv.visibility = View.VISIBLE
                binding.calendersRv.visibility = View.GONE
                if (response!!.message.toString() == "Authentication Token Expired"){
                    user!!.storeUserDetails("","","","","",""
                        ,"","","",""
                        ,"","","","",""
                        ,"","","")
                    startActivity(Intent(this@CalenderActivity, LoginActivity::class.java))
                    finish()
                }else{

                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateMonthYearText() {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        binding.dateTxt.text = displayedDate.format(formatter)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@CalenderActivity,HomeActivity::class.java)
        startActivity(intent)
        finish()
    }


}