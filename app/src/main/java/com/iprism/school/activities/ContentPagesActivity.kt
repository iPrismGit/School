package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.iprism.school.R
import com.iprism.school.databinding.ActivityContentPagesBinding

class ContentPagesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityContentPagesBinding
    var type :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentPagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
            var intent = Intent(this, InformationActivity::class.java)
             type = "Privacy Policy"
            intent.putExtra("type", type)
            startActivity(intent)
        })
    }

    private fun handleTermsAndConditionsLo() {
        binding.termsAndConditionsLo.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, InformationActivity::class.java)
            type = getString(R.string.terms_and_conditions)
            intent.putExtra("type", type)
            startActivity(intent)
        })
    }

    private fun handleAboutUsLo() {
        binding.aboutUsLo.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, InformationActivity::class.java)
            type = "About us"
            intent.putExtra("type", type)
            startActivity(intent)
        })
    }

}