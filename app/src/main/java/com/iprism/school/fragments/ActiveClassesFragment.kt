package com.iprism.school.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprism.school.R
import com.iprism.school.databinding.ActivityClassesBinding
import com.iprism.school.databinding.FragmentActiveClassesBinding

class ActiveClassesFragment : Fragment() {

    private lateinit var binding: FragmentActiveClassesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentActiveClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

}