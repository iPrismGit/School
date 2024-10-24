package com.iprism.school.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.iprism.school.R
import com.iprism.school.activities.HelpTutorialsActivity
import com.iprism.school.databinding.FragmentStaffScannerBinding
import com.iprism.school.databinding.FragmentStudentScannerBinding

class StudentScannerFragment : Fragment() {

    private lateinit var binding: FragmentStudentScannerBinding
    private var codeScanner: CodeScanner? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentScannerBinding.inflate(inflater, container, false)
        codeScanner = CodeScanner(requireContext(), binding.barcodeScanner)
        codeScanner!!.decodeCallback = DecodeCallback {
            val intent = Intent(context, HelpTutorialsActivity::class.java)
            startActivity(intent)
        }
        binding.barcodeScanner.setOnClickListener { codeScanner!!.startPreview() }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

}