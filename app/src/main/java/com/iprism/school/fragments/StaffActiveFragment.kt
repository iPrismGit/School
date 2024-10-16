package com.iprism.school.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.StaffAdapter
import com.iprism.school.databinding.ActivityCreateStaffBinding
import com.iprism.school.databinding.FragmentStaffActiveBinding

class StaffActiveFragment : Fragment() {

    private lateinit var binding: FragmentStaffActiveBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStaffActiveBinding.inflate(inflater, container, false)
        setupStaffAdapter()
        return binding.root
    }

    private fun setupStaffAdapter() {
        var staffAdapter = StaffAdapter(requireContext())
        binding.activeStaffRv.adapter = staffAdapter
        var linearLayoutManager = LinearLayoutManager(requireContext())
        binding.activeStaffRv.layoutManager = linearLayoutManager
    }

}