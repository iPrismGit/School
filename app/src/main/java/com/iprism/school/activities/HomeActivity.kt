package com.iprism.school.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.adapters.HomePagerAdapter
import com.iprism.school.databinding.ActivityHomeBinding
import com.iprism.school.fragments.HomeFragment
import com.iprism.school.fragments.MessagesFragment
import com.iprism.school.fragments.ScannerFragment
import com.iprism.school.model.Request.TeacherAccessReq
import com.iprism.school.model.Response.TeacherAccessResponse
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = HomePagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(0, false)
        handleBottomNav()
    }

    private fun handleBottomNav() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_nav -> binding.viewPager.setCurrentItem(0, false)
                R.id.messages_nav -> binding.viewPager.setCurrentItem(1, false)
                R.id.scanner_nav -> binding.viewPager.setCurrentItem(2, false)
                R.id.diary_nav -> binding.viewPager.setCurrentItem(3, false)
                R.id.help -> binding.viewPager.setCurrentItem(4, false)
            }
            true
        }
    }

    @SuppressLint("MissingSuperCall", "GestureBackNavigation")
    override fun onBackPressed() {
        val currentItem = binding.viewPager.currentItem

        if (currentItem != 0) {
            changeFragment(0)
        } else {
            if (backPressedOnce) {
                finishAffinity()
                return
            }

            backPressedOnce = true

            val snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "Are you sure you want to exit?",
                Snackbar.LENGTH_LONG
            )
                .setAction("Yes") {
                    finishAffinity()
                }

            snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.blue1))
            snackbar.setTextColor(ContextCompat.getColor(this, R.color.white))
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.white))
            snackbar.show()

            Handler(Looper.getMainLooper()).postDelayed({
                backPressedOnce = false
            }, 2000)
        }
    }

    private fun changeFragment(position: Int) {
        binding.viewPager.setCurrentItem(position, false)
        binding.bottomNavigationView.menu.getItem(position).isChecked = true
    }

}