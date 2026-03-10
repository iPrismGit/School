package com.iprism.school.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.iprism.school.R
import com.iprism.school.adapters.HomePagerAdapter
import com.iprism.school.adapters.MessagesPagerAdapter
import com.iprism.school.databinding.FragmentMessagesBinding

class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val adapter = MessagesPagerAdapter(this)
        binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.adapter = adapter
        binding.viewPager2.setCurrentItem(0, false)
        setupButtonsStyling(binding.classesBtn, binding.daycareBtn)
        handleClassesBtn()
        handleDayCareBtn()
        return binding.root
    }

    private fun handleClassesBtn() {
        binding.classesBtn.setOnClickListener { v ->
            binding.viewPager2.setCurrentItem(0, false)
            setupButtonsStyling(binding.classesBtn, binding.daycareBtn)
        }
    }

    private fun handleDayCareBtn() {
        binding.daycareBtn.setOnClickListener { v ->
            binding.viewPager2.setCurrentItem(1, false)
            setupButtonsStyling(binding.daycareBtn, binding.classesBtn)
        }
    }

    private fun setupButtonsStyling(
        classesBtn: TextView,
        daycareBtn: TextView
    ) {
        classesBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        classesBtn.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.filled_button_bg))
        daycareBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue1))
        daycareBtn.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bg_outline_button))
    }


}