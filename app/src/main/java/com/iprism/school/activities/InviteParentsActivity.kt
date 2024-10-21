package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.ClassTransfersAdapter
import com.iprism.school.databinding.ActivityInformationBinding
import com.iprism.school.databinding.ActivityInviteParentsBinding
import com.iprism.school.utils.ToastUtils

class InviteParentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInviteParentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInviteParentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupStudentsAdapter()
        handleAllStudentsLo()
        handleSelectedStudentsLo()
    }

    private fun handleAllStudentsLo() {
        binding.allStudentsLo.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Invite Sent to All Students !")
        })
    }

    private fun handleSelectedStudentsLo() {
        binding.selectedStudentsLo.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Invite Sent to Selected Students !")
        })
    }

    private fun setupStudentsAdapter() {
        var studentsAdapter = ClassTransfersAdapter(this)
        binding.studentsRv.adapter = studentsAdapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.studentsRv.layoutManager = linearLayoutManager
    }

}