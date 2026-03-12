package com.iprism.school.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.school.R
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivitySchoolSupportBinding
import com.iprism.school.model.contentpagesmodel.SchoolSupportApiRequest
import com.iprism.school.repositories.ContentPagesRepository
import com.iprism.school.repositories.HelpTutorialsRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.ContentPagesViewModel
import com.iprism.school.viewModels.HelpTutorialsViewModel
import com.iprism.school.viewModels.MessagesViewModel
import com.iprism.school.viewModels.ViewModelFactory

class SchoolSupportActivity : BaseActivity() {

    private lateinit var binding: ActivitySchoolSupportBinding
    private lateinit var viewModel: ContentPagesViewModel
    private var lat = 0.0
    private var lon = 0.0
    private var mobile = ""
    private var alternateMobile = ""
    private var email = ""
    private var address = ""
    private val CALL_PHONE_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySchoolSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        initViewModel()
        setupObservers()
        fetchSupportDetails()
        handleGetDirections()
        handleMobileTxt()
        handleEmailTxt()
        handleAlternateMobileNumberTxt()
    }

    private fun handleAlternateMobileNumberTxt() {
        binding.alternativeMobileTxt.setOnClickListener { view ->
            if (alternateMobile.length == 10) {
                makePhoneCall(alternateMobile)
            } else {
                ToastUtils.showErrorCustomToast(this, "Please Check The Alternative Mobile Number..!")
            }
        }
    }

    private fun handleEmailTxt() {
        binding.emailTxt.setOnClickListener { view ->
            if (email.isEmpty()){
                ToastUtils.showErrorCustomToast(this, "Email Not Found..!")
            } else{
                openEmail(email)
            }
        }
    }

    private fun handleMobileTxt() {
        binding.mobileTxt.setOnClickListener { view ->
            if (mobile.length == 10) {
                makePhoneCall(mobile)
            } else {
                ToastUtils.showErrorCustomToast(this, "Please Check The Mobile Number..!")
            }
        }
    }

    private fun handleGetDirections() {
        binding.directionsBtn.setOnClickListener { view ->
            if (address.isEmpty()){
                ToastUtils.showErrorCustomToast(this, "Address Not Found..!")
            } else{
                openDirections(lat, lon)
            }
        }
    }

    private fun fetchSupportDetails() {
        val request = SchoolSupportApiRequest(
            userDetails[User.SCHOOL_ID]!!,
            userDetails[User.ID]!!
        )
        viewModel.fetchSchoolSupportDetails(request)
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

    private fun initViewModel() {
        val repository = ContentPagesRepository(this)
        viewModel = ViewModelProvider(this, ViewModelFactory {
            ContentPagesViewModel(repository)
        })[ContentPagesViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        viewModel.schoolSupportResponse.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.mainLo.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.noDataFoundTxt.visibility = View.GONE
                    binding.mainLo.visibility = View.VISIBLE
                    binding.progress.hideProgress()
                    lat = state.data.response.lat
                    lon = state.data.response.lon
                    mobile = state.data.response.mobile
                    alternateMobile = state.data.response.alternate_mobile
                    email = state.data.response.email
                    address = state.data.response.address

                    if (email.isEmpty()) {
                        binding.emailTxt.text = "Email Not Available..!"
                    } else {
                        binding.emailTxt.text = email
                    }
                    if (mobile.equals("0", true)) {
                        binding.mobileTxt.text = "Mobile Number Not Available..!"
                    } else {
                        binding.mobileTxt.text = "+91 - $mobile"
                    }

                    if (alternateMobile.equals("0", true)) {
                        binding.alternativeMobileTxt.text =
                            "Alternative  Mobile Number Not Available..!"
                    } else {
                        binding.alternativeMobileTxt.text = "+91 - $alternateMobile"
                    }

                    if (address.isNotEmpty()) {
                        binding.addressTxt.text = address
                    } else {
                        binding.addressTxt.text = "Address Not Available..!"
                    }

                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    if (state.message.equals("no data found", true)) {
                        binding.noDataFoundTxt.visibility = View.VISIBLE
                        binding.mainLo.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun makePhoneCall(number: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CALL_PHONE
                )
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    CALL_PHONE_PERMISSION_CODE
                )
            } else {

                AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage("Calling permission is permanently denied. Please enable it in app settings.")
                    .setCancelable(false)
                    .setPositiveButton("Go to Settings") { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", packageName, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$number")
            startActivity(callIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CALL_PHONE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(mobile)
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage("Calling permission is required to make phone calls. Please enable it in app settings.")
                    .setCancelable(false)
                    .setPositiveButton("Go to Settings") { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", packageName, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    private fun openEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, "")
            putExtra(Intent.EXTRA_TEXT, "")
        }

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openDirections(lat: Double, lon: Double) {

        val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lon")

        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps") // Opens Google Maps directly
        }

        try {
            startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            // If Google Maps not installed, open in browser
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$lat,$lon")
            )
            startActivity(browserIntent)
        }
    }

}