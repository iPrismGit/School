package com.iprism.school.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.databinding.ActivityHomeBinding
import com.iprism.school.fragments.ChildCareFragment
import com.iprism.school.fragments.HomeFragment
import com.iprism.school.fragments.MessagesFragment
import com.iprism.school.fragments.ScannerFragment
import com.iprism.school.model.Request.LoginReq
import com.iprism.school.model.Request.TeacherAccessReq
import com.iprism.school.model.Response.OtpResponse
import com.iprism.school.model.Response.TeacherAccessResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var tag: String = ""
    private var emp_name: String = ""
    private var emp_id: String = ""

    private var teacherId: String = ""
    private var auth_token: String = ""

    private var onBackPressedListener: OnBackPressedListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleBottomNav()

        emp_name = userDetails[User.EMP_NAME].toString()
        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()

        checkPermissions()

        teacherAccess()

        if (intent.hasExtra("tag")) {
            tag = intent.getStringExtra("tag").toString()
        }
        if (tag.equals("msg", true) || tag.equals("msgInbox", true) || tag.equals("sent", true) || tag.equals("scheduled", true)) {
            binding.bottomNavigationView.selectedItemId = R.id.messages
            val messagesFragment = MessagesFragment()
            val bundle = Bundle()
            bundle.putString("tag", tag)
            messagesFragment.arguments = bundle
            switchFragment(messagesFragment)
        } else if (tag.equals("Dairy", true) || tag.equals("DayCare", true)) {
            binding.bottomNavigationView.selectedItemId = R.id.childcare
            val childFragment = ChildCareFragment()
            val bundle = Bundle()
            bundle.putString("tag", tag)
            childFragment.arguments = bundle
            switchFragment(childFragment)
            true
        }else if (tag == "from_qr") {
            switchFragment(ScannerFragment())
        }else {
            binding.bottomNavigationView.selectedItemId = R.id.home
            switchFragment(HomeFragment())
        }
    }

    private fun handleBottomNav() {
        binding.bottomNavigationView.setOnItemSelectedListener() { item ->
            when (item.itemId) {
                R.id.home -> {
                    switchFragment(HomeFragment())
                    true
                }

                R.id.messages -> {
                    val messagesFragment = MessagesFragment()
                    val bundle = Bundle()
                    bundle.putString("tag", "msgInbox") // Pass your value here
                    messagesFragment.arguments = bundle
                    switchFragment(messagesFragment)
                    true
                }

                R.id.scanner -> {
                    switchFragment(ScannerFragment())
                    true
                }

                R.id.childcare -> {
                    val childFragment = ChildCareFragment()
                    val bundle = Bundle()
                    bundle.putString("tag", "Dairy") // Pass your value here
                    childFragment.arguments = bundle
                    switchFragment(childFragment)
                    true
                }

                R.id.help -> {
                    startActivity(Intent(this, HelpTutorialsActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.flFragment, fragment)
            .commit();
    }


    private fun teacherAccess() {
        showProgress()
        var loginApiRequest = TeacherAccessReq( teacherId,auth_token )
        Log.d("accessRequest", loginApiRequest.toString())
        var call: Call<TeacherAccessResponse> = parentApiService!!.teacherAccess(loginApiRequest)
        call.enqueue(object : Callback<TeacherAccessResponse> {
            override fun onResponse(call: Call<TeacherAccessResponse>, response: Response<TeacherAccessResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()
//                        ToastUtils.showSuccessCustomToast(this@HomeActivity,"")


                    } else {
                        hideProgress()
//                        ToastUtils.showSuccessCustomToast(this@HomeActivity, loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","",""
                                ,"","","",""
                                ,"","","","",""
                                ,"","","")
                            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                            finish()
                        }else{

                        }

//                        ToastUtils.showErrorCustomToast(this@HomeActivity, loginApiResponse.message)
                    }
                } else {
                    hideProgress()
//                    ToastUtils.showErrorCustomToast(this@HomeActivity, "Failed")
                }
            }

            override fun onFailure(call: Call<TeacherAccessResponse>, t: Throwable) {
                hideProgress()
//                ToastUtils.showErrorCustomToast(this@HomeActivity, "Response Failed")
            }
        })
    }

    private fun checkPermissions() {
        val permissions = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        if (permissions.isNotEmpty()) {
            requestPermissionLauncher.launch(permissions.toTypedArray())
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.CAMERA] == true &&
                (permissions[Manifest.permission.READ_MEDIA_IMAGES] == true ||
                        permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true)) {
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show()
            }
        }

    interface OnBackPressedListener {
        fun doBack()
    }

    @JvmName("setOnBackPressedListener1")
    public fun setOnBackPressedListener(onBackPressedListener: OnBackPressedListener?) {
        this.onBackPressedListener = onBackPressedListener
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (onBackPressedListener != null)
            onBackPressedListener!!.doBack()
        else {
//            Are you sure you want to exit
            val snackbar = Snackbar.make(findViewById(android.R.id.content), "Are you sure you want to exit?..", Snackbar.LENGTH_LONG)
            val sbView = snackbar.view
            snackbar.setBackgroundTint(ContextCompat.getColor(this@HomeActivity, R.color.light_blue))
//            snackbar.setBackgroundColor(ContextCompat.getColor(this@HomeActivity, R.color.white))
            snackbar.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.white))
            snackbar.setActionTextColor(ContextCompat.getColor(this@HomeActivity, R.color.white))
            snackbar.setAction("Okay") {
                finishAffinity()
            }.show()
        }
    }


}