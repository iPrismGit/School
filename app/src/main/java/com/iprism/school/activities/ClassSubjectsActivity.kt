package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.ClassSubjectsAdapter
import com.iprism.school.databinding.ActivityClassSubjectsBinding
import com.iprism.school.databinding.ActivitySubjectsBinding
import com.iprism.school.databinding.ClassSubjectItemBinding

class ClassSubjectsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassSubjectsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassSubjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        setupClassSubjectsAdapter()
        handleAddBtn()
    }

    private fun handleAddBtn() {
        binding.addClassSubjectsBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CreateClassSubjectsActivity::class.java) )
        })
    }

    private fun setupClassSubjectsAdapter() {
        var classSubjectsAdapter = ClassSubjectsAdapter(this)
        binding.classSubjectsRv.adapter = classSubjectsAdapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.classSubjectsRv.layoutManager = linearLayoutManager
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}