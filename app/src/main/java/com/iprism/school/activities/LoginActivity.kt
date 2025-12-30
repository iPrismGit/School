package com.iprism.school.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityLoginBinding
import com.iprism.school.model.Request.LoginReq
import com.iprism.school.model.Request.OtpReq
import com.iprism.school.model.Response.LoginResponse
import com.iprism.school.model.Response.OtpResponse
import com.iprism.school.model.authmodel.LoginApiRequest
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.AuthenticationRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.AuthViewModel
import com.iprism.school.viewModels.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern
import kotlin.toString

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var currentOtp: String? = null
    private var mobile: String? = null
    private var login_type: String? = null
    var cTimer: CountDownTimer? = null
    private var playerId: String = ""
    private var countDownTime: String = ""
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        handleRequestOtpBtn()
        observeGenerateOtpResponse()
        handleResendBtn()
        handleContinueBtn()
        observeLoginResponse()
        buttonsStyling()
    }

    private fun buttonsStyling() {
        binding.mobileNumberEt.doOnTextChanged { text, _, _, _ ->
            binding.requestOtpBtn.isEnabled = text?.length == 10
        }

        binding.otpEt.doOnTextChanged { text, _, _, _ ->
            binding.loginBtn.isEnabled = text?.length == 4
        }
    }

    private fun observeLoginResponse() {
        viewModel.loginResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.loginBtn.isEnabled = false
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    var user = User(this)
                    binding.progress.hideProgress()
                    binding.loginBtn.isEnabled = true
                    user.storeNewUserDetails(result.data.id, result.data.first_name, result.data.middle_name, result.data.last_name, result.data.branch_id, result.data.mobile)
                    user.storeNewUserAuthToken(result.data.auth_token)
                    ToastUtils.showSuccessCustomToast(this, "Teacher Logged in Successfully!")
                    var intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()

                }

                is UiState.Error -> {
                    binding.loginBtn.isEnabled = true
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun initViewModel() {
        val repository = AuthenticationRepository()
        val factory = ViewModelFactory { AuthViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
    }

    private fun handleContinueBtn() {
        binding.loginBtn.setOnClickListener { view ->
            if (getOtp().length == 4) {
                if (getOtp() != currentOtp) {
                    ToastUtils.showErrorCustomToast(this, "Please Enter Valid Otp!")
                } else {
                    val loginRequest = LoginApiRequest(getMobileNumber(), "verified", playerId)
                    viewModel.loginUser(loginRequest)
                    Log.d("LoginApiRequest", loginRequest.toString())
                }
            } else {
                ToastUtils.showErrorCustomToast(this, "Please Enter 4 Digits Otp!")
            }
        }
    }

    private fun observeGenerateOtpResponse() {
        viewModel.otpResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.requestOtpBtn.isEnabled = false
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    currentOtp = result.data.otp
                    binding.mobileLo.visibility = View.GONE
                    binding.otpLl.visibility = View.VISIBLE
                    countDown()
                    ToastUtils.showSuccessCustomToast(this, currentOtp.toString())
                }

                is UiState.Error -> {
                    binding.requestOtpBtn.isEnabled = true
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }


    private fun handleRequestOtpBtn() {
        binding.requestOtpBtn.setOnClickListener {
            if (getMobileNumber().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Mobile Number..!")
            } else if (getMobileNumber().length != 10) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Mobile Number..!")
            } else if (Pattern.matches("[0-5].*", getMobileNumber())) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Mobile Number..")
            } else {
                var loginApiRequest = LoginApiRequest(getMobileNumber(), "not_verified", "1234")
                viewModel.generateOtp(loginApiRequest)
            }
        }
    }

    private fun getOtp(): String {
        return binding.otpEt.text.toString().trim()
    }

    private fun getMobileNumber(): String {
        return binding.mobileNumberEt.text.toString().trim()
    }

    private fun countDown() {
        object : CountDownTimer(40000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.countTxt.setText("00 : " + millisUntilFinished / 1000)
                countDownTime = (millisUntilFinished / 1000).toString() + "s"
            }

            override fun onFinish() {
                binding.countTxt.setText("00 : 00")
            }
        }.start()
    }


    private fun handleResendBtn() {
        binding.resendBtn.setOnClickListener(View.OnClickListener {
            val countDownTxt = binding.countTxt.text.toString()
            if (countDownTxt != "00 : 00") {
                Toast.makeText(
                    this,
                    "Please Try After $countDownTime To Resend OTP",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.resendBtn.isEnabled = false
                countDown()
//                var resendOtpApiRequest = ResendOtpApiRequest(mobileNumber)
//                viewModel.resendOtp(resendOtpApiRequest)
            }
        })
    }

    @SuppressLint("GestureBackNavigation")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}