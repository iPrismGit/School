package com.iprism.school.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.iprism.school.R
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityAddAttendanceBinding
import com.iprism.school.model.staffattendacemodel.StaffAttendanceApiRequest
import com.iprism.school.repositories.StaffAttendanceApiRepository
import com.iprism.school.utils.AbsentDayDecorator
import com.iprism.school.utils.HolidayDecorator
import com.iprism.school.utils.PresentDayDecoration
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.dismissDialog
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.utils.showProgressDialog
import com.iprism.school.viewModels.StaffAttendanceViewModel
import com.iprism.school.viewModels.ViewModelFactory
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AddAttendanceActivity : BaseActivity() {

    private lateinit var binding: ActivityAddAttendanceBinding
    private lateinit var codeScanner: CodeScanner
    private val CAMERA_REQUEST = 100
    var value = "in"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var backendDate: String = ""
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var currentTime: String = ""
    private lateinit var attendanceViewModel: StaffAttendanceViewModel
    private lateinit var progressDialog: ProgressDialog

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                if (::codeScanner.isInitialized) {
                    codeScanner.startPreview()
                }
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
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
        val formatterBackend = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        backendDate = LocalDate.now().format(formatterBackend)
        updateButtonStyling(binding.inTxt, binding.outTxt)
        initViewModel()
        setupScanner()
        setupClicks()
        checkCameraPermissionAndRequest()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermissionAndFetch()
        observeInsertAttendanceResponse()
    }

    private fun observeInsertAttendanceResponse() {
        attendanceViewModel.insertAttendanceResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    progressDialog = showProgressDialog("Attendance Adding Please Wait..!")
                }

                is UiState.Success -> {
                    progressDialog.dismissDialog()
                    var intent = Intent(this, SuccessActivity::class.java)
                    intent.putExtra("tag", value + " Added ")
                    startActivity(intent)
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    progressDialog.dismissDialog()
                }
            }
        }
    }

    private fun initViewModel() {
        val eventsRepository = StaffAttendanceApiRepository(this)
        val eventsFactory = ViewModelFactory { StaffAttendanceViewModel(eventsRepository) }
        attendanceViewModel =
            ViewModelProvider(this, eventsFactory)[StaffAttendanceViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchCurrentTime() {
        val formatter = DateTimeFormatter.ofPattern("hh : mm a")
        currentTime = LocalTime.now().format(formatter)

        Log.d("TIME", "Current Time: $currentTime")
    }

    private fun checkCameraPermissionAndRequest() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                codeScanner.startPreview()
            }

            else -> {
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }
        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupScanner() {
        codeScanner = CodeScanner(this, binding.barcodeScanner)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.CONTINUOUS
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false
            decodeCallback = DecodeCallback {
                runOnUiThread {
                    vibratePhone()
                    fetchCurrentTime()
                    //    Toast.makeText(this@AddAttendanceActivity, it.text, Toast.LENGTH_SHORT).show()
                    var request = StaffAttendanceApiRequest(
                        userDetails[User.ACADEMIC_YEAR_ID].toString(),
                        userDetails[User.SCHOOL_ID].toString(),
                        backendDate,
                        latitude.toString(),
                        longitude.toString(),
                        "",
                        "present",
                        currentTime,
                        value,
                        userDetails[User.ID].toString(),
                        "insert",
                        "",
                        it.text.toString()
                    )
                    Log.d("InsertRequest", request.toString())
                    attendanceViewModel.insertStaffAttendance(request)
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("ScannerError", it.message ?: "Camera error")
                }
            }
        }
    }

    private fun vibratePhone() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    getSystemService(android.os.VibratorManager::class.java)
                val vibrator = vibratorManager.defaultVibrator

                vibrator.vibrate(
                    android.os.VibrationEffect.createOneShot(
                        150,
                        android.os.VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                val vibrator = getSystemService(VIBRATOR_SERVICE) as android.os.Vibrator
                @Suppress("DEPRECATION")
                vibrator.vibrate(150)
            }
        } catch (e: Exception) {
            Log.e("VIBRATION", "Vibration failed", e)
        }
    }

    private fun checkLocationPermissionAndFetch() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation()
            }

            else -> {
                locationPermissionRequest.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
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

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude

                Log.d("LOCATION", "Lat: $latitude , Lon: $longitude")
            } else {
                requestFreshLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestFreshLocation() {
        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000
        ).setMaxUpdates(1)
            .build()

        fusedLocationClient.requestLocationUpdates(
            request,
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val location = result.lastLocation ?: return

                    latitude = location.latitude
                    longitude = location.longitude

                    Log.d(
                        "LOCATION",
                        "Lat: $latitude, Lon: $longitude"
                    )

                    fusedLocationClient.removeLocationUpdates(this)
                }
            },
            Looper.getMainLooper()
        )
    }

}