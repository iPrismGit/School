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

        })
    }

    private fun handleTermsAndConditionsLo() {
        binding.termsAndConditionsLo.setOnClickListener(View.OnClickListener {

        })
    }

    private fun handleAboutUsLo() {
        binding.aboutUsLo.setOnClickListener(View.OnClickListener {
        })
    }

}