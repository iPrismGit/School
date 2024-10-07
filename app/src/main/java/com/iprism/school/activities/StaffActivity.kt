package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.iprism.school.adapters.StaffTabsAdapter
import com.iprism.school.databinding.ActivityStaffBinding

class StaffActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStaffBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupTabs()
        handleAddStaffFb()
    }

    private fun setupTabs() {
        val staffTabsAdapter = StaffTabsAdapter(this)
        binding.viewPager.adapter = staffTabsAdapter
        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Active"
                1 -> tab.text = "Inactive"
                else -> tab.text = "Inactive"
            }
        }
        tabLayoutMediator.attach()
    }

    private fun handleAddStaffFb() {
        binding.addStaffFb.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CreateStaffActivity::class.java))
        })
    }
}