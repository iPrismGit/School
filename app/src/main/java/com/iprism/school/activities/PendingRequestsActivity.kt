package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.PendingRequestsAdapter
import com.iprism.school.databinding.ActivityPendingRequestsBinding
import com.iprism.school.databinding.MessageDeliveryItemBinding
import com.iprism.school.interfaces.PendingRequestClickListener

class PendingRequestsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPendingRequestsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupPendingRequestsRv()
        handleBack()
    }



    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun setupPendingRequestsRv() {
        var pendingRequestsAdapter = PendingRequestsAdapter(this)
        binding.pendingRequestsRv.adapter = pendingRequestsAdapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.pendingRequestsRv.layoutManager = linearLayoutManager
        pendingRequestsAdapter.setupListener(object : PendingRequestClickListener{
            override fun onPendingItemClick(pendingRequestId: String) {
                var intent =Intent(this@PendingRequestsActivity, PendingRequestDetailsActivity::class.java)
                intent.putExtra("pendingRequestId", pendingRequestId)
                startActivity(intent)
            }
        })
    }

}