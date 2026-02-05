package com.iprism.school.activities

import android.os.Bundle
import android.view.View
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityContentPagesBinding
import com.iprism.school.utils.User

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