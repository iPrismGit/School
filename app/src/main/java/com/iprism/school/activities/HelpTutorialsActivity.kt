package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.HelpTutorialsAdapter
import com.iprism.school.databinding.ActivityHelpTutorialsBinding
import com.iprism.school.databinding.AlbumItemBinding

class HelpTutorialsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpTutorialsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpTutorialsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupTutorialsAdapter()
        handleBack()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun setupTutorialsAdapter() {
        var tutorialsAdapter = HelpTutorialsAdapter(this)
        binding.tutorialVideosRv.adapter = tutorialsAdapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.tutorialVideosRv.layoutManager = linearLayoutManager
    }

}