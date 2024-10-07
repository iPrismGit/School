package com.iprism.school.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.iprism.school.R
import com.iprism.school.adapters.ConsentsTabsAdapter
import com.iprism.school.adapters.ScannerTabsAdapter
import com.iprism.school.databinding.FragmentCabScannerBinding
import com.iprism.school.databinding.FragmentScannerBinding

class ScannerFragment : Fragment() {

    private lateinit var binding: FragmentScannerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScannerBinding.inflate(inflater, container, false)
        setUpTabs()
        return binding.root
    }

    private fun setUpTabs() {
        val consentsTabsAdapter = ScannerTabsAdapter(this)
        binding.viewPager.adapter = consentsTabsAdapter
        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "STAFF"
                    1 -> tab.text = "STUDENT"
                    2 -> tab.text = "CAB"
                    else -> tab.text = "STAFF"
                }
            }
        tabLayoutMediator.attach()
    }

}