package com.iprism.school.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.iprism.school.R
import com.iprism.school.databinding.ActivityAddAttendanceBinding

class AddAttendanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAttendanceBinding
    private lateinit var codeScanner: CodeScanner
    private val CAMERA_REQUEST = 100
    var value = "in"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        updateButtonStyling(binding.inTxt, binding.outTxt)
        setupScanner()
        setupClicks()
        checkCameraPermission()
    }

    private fun setupClicks() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
        binding.barcodeScanner.setOnClickListener {
            codeScanner.startPreview()
        }

        binding.inTxt.setOnClickListener {
            updateButtonStyling(binding.inTxt, binding.outTxt)
            value = "in"
        }

        binding.outTxt.setOnClickListener {
            updateButtonStyling(binding.outTxt, binding.inTxt)
            value = "out"
        }
    }


    private fun setupScanner() {
        codeScanner = CodeScanner(this, binding.barcodeScanner)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            decodeCallback = DecodeCallback {
                runOnUiThread {
                    Toast.makeText(this@AddAttendanceActivity, it.text, Toast.LENGTH_SHORT).show()
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("ScannerError", it.message ?: "Camera error")
                }
            }
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            codeScanner.startPreview()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        if (::codeScanner.isInitialized) {
            codeScanner.releaseResources()
        }
        super.onPause()
    }

    private fun updateButtonStyling(textView: TextView, textView1: TextView) {
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        textView1.setTextColor(ContextCompat.getColor(this, R.color.black))
        textView.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.filled_button_bg
            )
        )
        textView1.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.transparent_btn_bg
            )
        )
    }


}