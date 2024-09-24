package com.iprism.school.activities

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.CalenderAdapter
import com.iprism.school.databinding.ActivityCalenderBinding
import com.iprism.school.interfaces.OnCalenderClickListener
import java.util.Locale

class CalenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalenderBinding
    private val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDate()
        handleBack()
        handleAddCalenderBtn()
        setupCalenderAdapter()
        handleRightBtn()
        hanldeLeftBtn()
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

    private fun setupCalenderAdapter() {
        var calenderAdapter = CalenderAdapter(this)
        binding.calendersIv.adapter = calenderAdapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.calendersIv.layoutManager = linearLayoutManager
        calenderAdapter.setListener(object : OnCalenderClickListener {
            override fun onItemClick(calenderId: String) {
                val intent = Intent(this@CalenderActivity, CalenderDetailsActivity::class.java)
                intent.putExtra("calenderId", calenderId)
                startActivity(intent)
            }

        })
    }

    private fun handleAddCalenderBtn() {
        binding.addCalenderBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CreateCalenderActivity::class.java))
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}