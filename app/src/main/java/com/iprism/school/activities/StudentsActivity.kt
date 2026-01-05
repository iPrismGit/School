package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.adapters.StudentsListAdapter
import com.iprism.school.adapters.StudentsPagerAdapter
import com.iprism.school.databinding.ActivityStudentsBinding
import com.iprism.school.model.Request.StudentsListReq
import com.iprism.school.model.Response.StudentListResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentsActivity : BaseActivity() {

    private lateinit var binding: ActivityStudentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = StudentsPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(0, false)
        handleBack()
        handleActiveStudentsLo()
        handleInActiveStudentsLo()
    }

    private fun handleActiveStudentsLo() {
        binding.activeStudentsLo.setOnClickListener(View.OnClickListener {
            setupButtonsStyling(binding.activeStudentsLo, binding.inactiveStudentsLo)
            binding.viewPager.setCurrentItem(0, false)
        })
    }

    private fun handleInActiveStudentsLo() {
        binding.inactiveStudentsLo.setOnClickListener(View.OnClickListener {
            setupButtonsStyling(binding.inactiveStudentsLo, binding.activeStudentsLo)
            binding.viewPager.setCurrentItem(1, false)
        })
    }

    private fun setupButtonsStyling(textView: TextView, textView1: TextView){
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        textView.setBackgroundColor(ContextCompat.getColor(this, R.color.blue1))
        textView1.setTextColor(ContextCompat.getColor(this, R.color.black))
        textView1.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}