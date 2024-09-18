package com.iprism.school.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.iprism.school.databinding.ActivityCreateConsentBinding
import com.iprism.school.model.ClassInfo
import com.iprism.school.utils.DateTimeUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CreateConsentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateConsentBinding
    private var selectedValue: String = ""
    private lateinit var classInfoList: List<ClassInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateConsentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        classInfoList = getClassInfoList()
        setupCheckboxes(binding.checkBox6, binding.checkBox7)
        setDateAndTime()
        handleBack()
        handleTimeLo()
        handleDateLo()
        handleSendBtn()
    }


    private fun handleSendBtn() {
        binding.sendBtn.setOnClickListener(View.OnClickListener {
            Log.d("selectedValue", selectedValue)
            finish()
        })
    }

    private fun setupCheckboxes(checkBox1: CheckBox, checkBox2: CheckBox) {
        checkBox1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBox2.isChecked = false
                selectedValue = "Consent"
            } else if (!checkBox2.isChecked) {
                selectedValue = ""
            }
        }

        checkBox2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBox1.isChecked = false
                selectedValue = "Invitation"
            } else if (!checkBox1.isChecked) {
                selectedValue = ""
            }
        }
    }

    private fun setDateAndTime() {
        val calendar: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val stf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedDate: String = sdf.format(calendar.getTime())
        val formattedTime = stf.format(calendar.time)
        binding.dateTxt.text = formattedDate
        binding.timeTxt.text = formattedTime
    }

    private fun handleDateLo() {
        binding.dateLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getDate(binding.dateTxt, false)
            Log.d("dateTag", DateTimeUtils.dateMonthYear)
        })
    }

    private fun handleTimeLo() {
        binding.timeLo.setOnClickListener(View.OnClickListener {
            DateTimeUtils.getTime(binding.timeTxt)
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun getClassInfoList(): List<ClassInfo> {
        return listOf(
            ClassInfo("Tap Here To Add Classes", 101),
            ClassInfo("Science", 102),
            ClassInfo("History", 103),
            ClassInfo("English", 104)
        )
    }

}