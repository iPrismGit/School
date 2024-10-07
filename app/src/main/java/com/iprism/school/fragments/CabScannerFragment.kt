package com.iprism.school.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprism.school.R
import com.iprism.school.databinding.FragmentCabScannerBinding
import com.iprism.school.databinding.FragmentStudentScannerBinding

class CabScannerFragment : Fragment() {

    private lateinit var binding: FragmentCabScannerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCabScannerBinding.inflate(inflater, container, false)

        return binding.root
    }

}