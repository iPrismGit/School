package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iprism.school.R
import com.iprism.school.databinding.ActivityAboutUsBinding
import com.iprism.school.databinding.HelpTutorialItemBinding

class AboutUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutUsBinding
    private var tag: String = ""
    private var name: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleTermsLo()
        handlePrivacyPolicyLo()
        handleAboutUsLo()
        handleBack()
    }

    private fun handleTermsLo() {
        binding.termsAndConditionsLo.setOnClickListener { view ->
            tag = "terms"
            name = "Terms & Conditions"
            var intent = Intent(this, ContentPagesActivity::class.java)
            intent.putExtra("tag", tag)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }

    private fun handlePrivacyPolicyLo() {
        binding.privacyPolicyLo.setOnClickListener { view ->
            tag = "privacy"
            name = "Privacy Policy"
            var intent = Intent(this, ContentPagesActivity::class.java)
            intent.putExtra("tag", tag)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }

    private fun handleAboutUsLo() {
        binding.aboutUsLo.setOnClickListener { view ->
            tag = "about_us"
            name = "About Us"
            var intent = Intent(this, ContentPagesActivity::class.java)
            intent.putExtra("tag", tag)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener {
            finish()
        }
    }

}