package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.databinding.ActivityContentPagesBinding
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Response.AboutUsResponse
import com.iprism.school.model.Response.PrivacyResponse
import com.iprism.school.model.Response.TermsandConditionResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContentPagesActivity : BaseActivity() {

    private lateinit var binding : ActivityContentPagesBinding
    var type :String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentPagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        handleBack()
        handleAboutUsLo()
        handleTermsAndConditionsLo()
        handlePrivacyPolicy()

    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handlePrivacyPolicy() {
        binding.privacyPolicyLo.setOnClickListener(View.OnClickListener {

            privacy()

//            var intent = Intent(this, InformationActivity::class.java)
//             type = "Privacy Policy"
//            intent.putExtra("type", type)
//            startActivity(intent)
        })
    }

    private fun handleTermsAndConditionsLo() {
        binding.termsAndConditionsLo.setOnClickListener(View.OnClickListener {
            tc()
//            var intent = Intent(this, InformationActivity::class.java)
//            type = getString(R.string.terms_and_conditions)
//            intent.putExtra("type", type)
//            startActivity(intent)
        })
    }

    private fun handleAboutUsLo() {
        binding.aboutUsLo.setOnClickListener(View.OnClickListener {

            aboutUs()
//            var intent = Intent(this, InformationActivity::class.java)
//            type = "About us"
//            intent.putExtra("type", type)
//            startActivity(intent)
        })
    }

    private fun aboutUs() {
        showProgress()
        var apiRequest = SchoolStaffReq(auth_token,scl_id,teacherId)
        Log.d("privacy", apiRequest.toString())
        val call: Call<AboutUsResponse> = parentApiService!!.aboutUs(apiRequest)
        call.enqueue(object : Callback<AboutUsResponse> {
            override fun onResponse(call: Call<AboutUsResponse>, response: Response<AboutUsResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val contentTv = loginApiResponse.response.aboutus[0].content.toString()

//                        var intent = Intent(this@ContentPagesActivity, InformationActivity::class.java)
//                        type = "About us"
//                        intent.putExtra("type", type)
//                        intent.putExtra("contentTv", contentTv)
//                        startActivity(intent)

                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@ContentPagesActivity, response.message())
                }
            }
            override fun onFailure(call: Call<AboutUsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@ContentPagesActivity, t.message.toString())
            }
        })
    }

    private fun privacy() {
        showProgress()
        var apiRequest = SchoolStaffReq(auth_token,scl_id,teacherId)
        Log.d("privacy", apiRequest.toString())
        val call: Call<PrivacyResponse> = parentApiService!!.privacy(apiRequest)
        call.enqueue(object : Callback<PrivacyResponse> {
            override fun onResponse(call: Call<PrivacyResponse>, response: Response<PrivacyResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val contentTv = loginApiResponse.response.privacypolicy[0].content.toString()

//                        var intent = Intent(this@ContentPagesActivity, InformationActivity::class.java)
//                        type = "Privacy Policy"
//                        intent.putExtra("type", type)
//                        intent.putExtra("contentTv", contentTv)
                        startActivity(intent)

                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@ContentPagesActivity, response.message())
                }
            }
            override fun onFailure(call: Call<PrivacyResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@ContentPagesActivity, t.message.toString())
            }
        })
    }

    private fun tc() {
        showProgress()
        var apiRequest = SchoolStaffReq(auth_token,scl_id,teacherId)
        Log.d("privacy", apiRequest.toString())
        val call: Call<TermsandConditionResponse> = parentApiService!!.termsandcondition(apiRequest)
        call.enqueue(object : Callback<TermsandConditionResponse> {
            override fun onResponse(call: Call<TermsandConditionResponse>, response: Response<TermsandConditionResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val contentTv = loginApiResponse.response.termsandconditions[0].content.toString()

//                        var intent = Intent(this@ContentPagesActivity, InformationActivity::class.java)
//                        type = getString(R.string.terms_and_conditions)
//                        intent.putExtra("type", type)
//                        intent.putExtra("contentTv", contentTv)
//                        startActivity(intent)

                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@ContentPagesActivity, response.message())
                }
            }
            override fun onFailure(call: Call<TermsandConditionResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@ContentPagesActivity, t.message.toString())
            }
        })
    }

}