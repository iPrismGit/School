package com.iprism.school.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.iprism.school.R
import com.iprism.school.adapters.PagerAdapter
import com.iprism.school.databinding.FragmentChildCareBinding

class ChildCareFragment : Fragment() {

    private lateinit var binding : FragmentChildCareBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChildCareBinding.inflate(layoutInflater)
        setupTabs()
        return binding.root
    }

    private fun setupTabs() {
        val pagerAdapter = PagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Dairy"
                1 -> tab.text = "Day Care"
                else -> tab.text = "Day Care"
            }
        }
        tabLayoutMediator.attach()
    }
}