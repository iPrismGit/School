package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.school.R
import com.iprism.school.adapters.AlbumsAdapter
import com.iprism.school.databinding.ActivityAlbumsBinding
import com.iprism.school.databinding.ActivityCreateAlbumsBinding

class AlbumsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleAddBtn()
        setupAlbumsAdapter()
    }

    private fun setupAlbumsAdapter() {
        var albumsAdapter = AlbumsAdapter(this)
        binding.albumsRv.adapter = albumsAdapter
        var linearLayoutManager = GridLayoutManager(this, 2)
        binding.albumsRv.layoutManager = linearLayoutManager
    }

    private fun handleAddBtn() {
        binding.addBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CreateAlbumsActivity::class.java))
        })
    }


}