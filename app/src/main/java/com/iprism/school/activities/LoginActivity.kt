package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.databinding.ActivityLoginBinding
import com.iprism.school.model.Request.LoginReq
import com.iprism.school.model.Request.OtpReq
import com.iprism.school.model.Response.LoginResponse
import com.iprism.school.model.Response.OtpResponse
import com.iprism.school.utils.ToastUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var currentOtp : String? = null
    private var mobile : String? = null
    private var login_type : String? = null
    var cTimer: CountDownTimer? = null
    private var playerId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleRequestOtpBtn()

        binding.resendTv.setOnClickListener {
            if (getMobileNumber().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Mobile Number!")
            } else if (getMobileNumber().length != 10) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Mobile Number!")
            } else if (Pattern.matches("[0-5].*", getMobileNumber())) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Mobile Number")
            } else {
                resendOtp(getMobileNumber())
            }
        }


        binding.loginBtn.setOnClickListener {
            if (binding.otpEt.length() == 4) {
                if (binding.otpEt.text.toString() != currentOtp) {
                    ToastUtils.showErrorCustomToast(this, "Please Enter Valid Otp !")
                } else {
                    loginUser(binding.mobileNumberEt.text.toString())
                }
            }
        }

    }


    private fun handleRequestOtpBtn() {
        binding.requestOtpBtn.setOnClickListener(View.OnClickListener {
            if (getMobileNumber().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Mobile Number!")
            } else if (getMobileNumber().length != 10) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Mobile Number!")
            } else if (Pattern.matches("[0-5].*", getMobileNumber())) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Valid Mobile Number")
            } else {
                generateOtp(getMobileNumber())
            }
        })
    }

    private fun generateOtp(mobileNumber: String) {
        showProgress()
        var loginApiRequest = LoginReq(mobileNumber, "no", "token")
        val gson = Gson()
        val json = gson.toJson(loginApiRequest)
        Log.d("otpApiRequest", json)

        var call: Call<OtpResponse> = parentApiService!!.loginOTP(loginApiRequest)
        call.enqueue(object : Callback<OtpResponse> {
            override fun onResponse(call: Call<OtpResponse>, response: Response<OtpResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()

                        currentOtp = loginApiResponse.response.otp.toString()
                        ToastUtils.showSuccessCustomToast(this@LoginActivity, currentOtp.toString())

                        binding.otpLl.visibility = View.VISIBLE
                        binding.requestOtpBtn.visibility = View.GONE
                        startTimer()

                    } else {
                        binding.otpLl.visibility = View.GONE
                        binding.requestOtpBtn.visibility = View.VISIBLE
                        hideProgress()
                        ToastUtils.showErrorCustomToast(this@LoginActivity, loginApiResponse.message)
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@LoginActivity, "Failed")
                }
            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@LoginActivity, "Response Failed")
            }
        })
    }

    private fun resendOtp(mobileNumber: String) {
        showProgress()
        var loginApiRequest = OtpReq(mobileNumber)
        Log.d("LoginApiRequest", loginApiRequest.toString())
        var call: Call<OtpResponse> = parentApiService!!.reSendOtp(loginApiRequest)
        call.enqueue(object : Callback<OtpResponse> {
            override fun onResponse(call: Call<OtpResponse>, response: Response<OtpResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()

                        currentOtp = loginApiResponse.response.otp.toString()
                        ToastUtils.showSuccessCustomToast(this@LoginActivity, currentOtp.toString())

                        binding.otpLl.visibility = View.VISIBLE
                        binding.requestOtpBtn.visibility = View.GONE
                        startTimer()

                    } else {
                        binding.otpLl.visibility = View.GONE
                        binding.requestOtpBtn.visibility = View.VISIBLE
                        hideProgress()
                        ToastUtils.showErrorCustomToast(this@LoginActivity, loginApiResponse.message)
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@LoginActivity, "Failed")
                }
            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@LoginActivity, "Response Failed")
            }
        })
    }


    private fun loginUser(mobileNumber: String) {
        showProgress()
        var loginApiRequest = LoginReq(mobileNumber,"yes","hjeffe")
        Log.d("LoginApiRequest", loginApiRequest.toString())
        var call: Call<LoginResponse> = parentApiService!!.loginUser(loginApiRequest)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    var loginApiResponse = response.body()
                    if (loginApiResponse!!.status) {
                        hideProgress()

                        user?.storeUserDetails(
                            loginApiResponse.response.teacher_details[0].id.toString(),
                            loginApiResponse.response.teacher_details[0].school_id.toString(),
                            loginApiResponse.response.teacher_details[0].auth_token.toString(),
                            loginApiResponse.response.teacher_details[0].token.toString(),
                            loginApiResponse.response.teacher_details[0].employee_mobile.toString(),
                            loginApiResponse.response.teacher_details[0].employee_id.toString(),
                            loginApiResponse.response.teacher_details[0].employee_name.toString(),
                            loginApiResponse.response.teacher_details[0].employee_email.toString(),
                            loginApiResponse.response.teacher_details[0].employee_dob.toString(),
                            loginApiResponse.response.teacher_details[0].employee_gender.toString(),
                            loginApiResponse.response.teacher_details[0].employee_image.toString(),
                            loginApiResponse.response.teacher_details[0].employee_designation.toString(),
                            loginApiResponse.response.teacher_details[0].employee_class.toString(),
                            loginApiResponse.response.teacher_details[0].employee_department.toString(),
                            loginApiResponse.response.teacher_details[0].employee_use_designation.toString(),
                            loginApiResponse.response.teacher_details[0].delete_status.toString(),
                            loginApiResponse.response.teacher_details[0].created_on.toString()
                            ,loginApiResponse.response.teacher_details[0].updated_on.toString()
                        )

                        val intent = Intent(this@LoginActivity,HomeActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        binding.otpLl.visibility = View.GONE
                        binding.requestOtpBtn.visibility = View.VISIBLE
                        hideProgress()
                        ToastUtils.showErrorCustomToast(this@LoginActivity, loginApiResponse.message)
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@LoginActivity, "Failed")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@LoginActivity, "Response Failed")
            }
        })
    }

    private fun getMobileNumber(): String {
        return binding.mobileNumberEt.text.toString().trim()
    }


    private fun startTimer() {
        cTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.resendTv.text = "00 : " + (millisUntilFinished / 1000).toString()+" Sec"
                binding.resendTv.visibility = View.VISIBLE
//                binding.requestOtpBtn.visibility = View.GONE
            }

            override fun onFinish() {
                binding.resendTv.visibility = View.VISIBLE
                binding.resendTv.text = "Resend"
//                binding.requestOtpBtn.visibility = View.GONE
//                tv.setText("Re send OTP!")
//                resend.setEnabled(true)
            }
        }
        (cTimer as CountDownTimer).start()
    }


    private fun handleRequestOtp() {
        binding.requestOtpBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, OtpVerificationActivity::class.java))
        })
    }




    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}