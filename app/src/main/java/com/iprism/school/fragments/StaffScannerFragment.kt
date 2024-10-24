package com.iprism.school.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.iprism.school.R
import com.iprism.school.activities.HelpTutorialsActivity
import com.iprism.school.databinding.ActivityFeedBackBinding
import com.iprism.school.databinding.FragmentStaffScannerBinding

class StaffScannerFragment : Fragment() {

    private lateinit var binding: FragmentStaffScannerBinding
    private var codeScanner: CodeScanner? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStaffScannerBinding.inflate(inflater, container, false)

        codeScanner = CodeScanner(requireContext(), binding.barcodeScanner)
        codeScanner!!.decodeCallback = DecodeCallback {
            vibratePhone()
            val intent = Intent(context, HelpTutorialsActivity::class.java)
            startActivity(intent)
        }
        binding.barcodeScanner.setOnClickListener { codeScanner!!.startPreview() }
        return binding.root
    }

    private fun vibratePhone() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }
        }
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