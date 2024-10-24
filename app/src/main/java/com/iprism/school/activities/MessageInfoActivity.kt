package com.iprism.school.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.DeliveryReportsAdapter
import com.iprism.school.adapters.MessageDeliveryAdapter
import com.iprism.school.databinding.ActivityMessageBinding
import com.iprism.school.databinding.ActivityMessageInfoBinding

class MessageInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageInfoBinding
    private var messageId : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        messageId = intent.getStringExtra("messageId").toString()
        handleBack()
        handleSearchLo()
        handleCross()
        handleBack()
        setupMessageDeliveryAdapter()
    }

    private fun setupMessageDeliveryAdapter() {
        var messageDeliveryAdapter = MessageDeliveryAdapter(this)
        binding.deliveryReportsRv.adapter = messageDeliveryAdapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.deliveryReportsRv.layoutManager = linearLayoutManager
    }

    private fun handleCross() {
        binding.crossIv.setOnClickListener(View.OnClickListener {
            binding.toolBarLo.visibility = View.VISIBLE
            binding.searchLo.visibility = View.GONE
            binding.searchTxt.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchTxt.windowToken, 0)
        })
    }

    private fun handleSearchLo() {
        binding.searchIv.setOnClickListener(View.OnClickListener {
            binding.toolBarLo.visibility = View.GONE
            binding.searchLo.visibility = View.VISIBLE
            binding.searchTxt.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchTxt, InputMethodManager.SHOW_IMPLICIT)
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}