package com.iprism.school.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.iprism.school.R
import com.iprism.school.adapters.PagerAdapter
import com.iprism.school.databinding.FragmentChildCareBinding

class ChildCareFragment : Fragment() {

    private lateinit var binding: FragmentChildCareBinding
    private var tag: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildCareBinding.inflate(layoutInflater)
        tag = arguments?.getString("tag").toString()
        Log.d("tagFragment", tag)
        setupTabs()
        if (tag != null) {
            binding.viewPager.currentItem = if (tag == "Dairy") 0 else 1
        }
        return binding.root
    }

    private fun setupTabs() {
        val pagerAdapter = PagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Dairy"
                    1 -> tab.text = "Day Care"
                    else -> tab.text = "Day Care"
                }
            }
        tabLayoutMediator.attach()
    }

}