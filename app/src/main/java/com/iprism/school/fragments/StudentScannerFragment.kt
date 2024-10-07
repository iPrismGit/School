package com.iprism.school.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprism.school.R
import com.iprism.school.databinding.FragmentStaffScannerBinding
import com.iprism.school.databinding.FragmentStudentScannerBinding

class StudentScannerFragment : Fragment() {

    private lateinit var binding: FragmentStudentScannerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

}