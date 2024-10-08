package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.StudentsAdapter
import com.iprism.school.databinding.ActivityStudentsBinding
import com.iprism.school.interfaces.OnCalenderClickListener

class StudentsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStudentsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        handleStudent()
        setupStudentsAdapter()
    }

    private fun setupStudentsAdapter() {
        var studentsAdapter = StudentsAdapter(this)
        binding.studentsRv.adapter = studentsAdapter
        var layoutManager = LinearLayoutManager(this)
        binding.studentsRv.layoutManager = layoutManager
        studentsAdapter.setListener(object : OnCalenderClickListener{
            override fun onItemClick(calenderId: String) {
                startActivity(Intent(this@StudentsActivity, StudentDetailsActivity::class.java))
            }

        })
    }

//    private fun handleStudent() {
//        binding.linearLayout4.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(this, StudentDetailsActivity::class.java))
//        })
//    }
}