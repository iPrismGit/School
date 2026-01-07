package com.iprism.school.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iprism.school.R
import com.iprism.school.databinding.ActivityPdfViewBinding
import com.iprism.school.utils.Constants

class PdfViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfViewBinding
    private var pdfUrl: String = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPdfViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        pdfUrl = intent.getStringExtra("pdfUrl").toString()
        binding.pdfView.settings.javaScriptEnabled = true
        binding.progress.visibility = View.VISIBLE
        val googleDocsUrl = "https://docs.google.com/viewer?url=${Constants.IMAGES_URL + pdfUrl}"
        binding.pdfView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progress.visibility = View.GONE
            }
        }

        binding.pdfView.loadUrl(googleDocsUrl)
        handleBack()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}