package com.iprism.school.fragments

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.DairiesAdapter
import com.iprism.school.databinding.FragmentDiaryBinding

class DiaryFragment : Fragment() {

    private lateinit var binding : FragmentDiaryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDiaryBinding.inflate(inflater, container, false)
        setupDiaryAdapter()
        return binding.root
    }

    private fun setupDiaryAdapter() {
        var dairiesAdapter = DairiesAdapter(requireContext())
        binding.dairyRv.adapter = dairiesAdapter
        var layoutManager = LinearLayoutManager(requireContext())
        binding.dairyRv.layoutManager = layoutManager
    }

}