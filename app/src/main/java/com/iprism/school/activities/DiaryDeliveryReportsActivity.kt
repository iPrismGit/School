package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.DeliveryReportsAdapter
import com.iprism.school.databinding.ActivityDiaryDeliveryReportsBinding

class DiaryDeliveryReportsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryDeliveryReportsBinding
    var diaryId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryDeliveryReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        diaryId = intent.getStringExtra("diaryId").toString()
        setupDeliveryReportsAdapter()
        handleBack()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun setupDeliveryReportsAdapter() {
        var deliveryReportsAdapter = DeliveryReportsAdapter(this)
        binding.deliveryReportsRv.adapter = deliveryReportsAdapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.deliveryReportsRv.layoutManager = linearLayoutManager
    }

}