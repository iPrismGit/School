package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.iprism.school.R
import com.iprism.school.adapters.ConsentsTabsAdapter
import com.iprism.school.adapters.PagerAdapter
import com.iprism.school.databinding.ActivityConsentsBinding

class ConsentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsentsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack();
        setUpTabs()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun setUpTabs() {
        val consentsTabsAdapter = ConsentsTabsAdapter(this)
        binding.viewPager.adapter = consentsTabsAdapter
        val tabLayoutMediator =
            TabLayoutMediator(binding.tabsLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "ACTIVE"
                    1 -> tab.text = "INACTIVE"
                    else -> tab.text = "INACTIVE"
                }
            }
        tabLayoutMediator.attach()
    }

}

