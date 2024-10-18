package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.ClassTransfersAdapter
import com.iprism.school.databinding.ActivityClassTransferBinding
import com.iprism.school.databinding.ActivityClassesBinding
import com.iprism.school.utils.ToastUtils

class ClassTransferActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassTransferBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        setupStudentsAdapter()
        handleTransferBtn()
    }

    private fun handleTransferBtn() {
        binding.transferBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Students Successfully Transfered")
            finish()
        })
    }

    private fun setupStudentsAdapter() {
        var classTransfersAdapter = ClassTransfersAdapter(this)
        binding.studentsRv.adapter = classTransfersAdapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.studentsRv.layoutManager = linearLayoutManager
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}