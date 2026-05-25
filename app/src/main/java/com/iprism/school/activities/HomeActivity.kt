package com.iprism.school.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.adapters.HomePagerAdapter
import com.iprism.school.databinding.ActivityHomeBinding
import com.iprism.school.utils.InAppUpdate


class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var tag = ""
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        InAppUpdate.initUpdate(this)
        tag = intent.getStringExtra("tag").toString()
        val adapter = HomePagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        if (tag.isEmpty()) {
            binding.bottomNavigationView.selectedItemId = R.id.home_nav
            binding.viewPager.setCurrentItem(0, false)
        } else if (tag.equals("Tutorial", true)) {
            binding.bottomNavigationView.selectedItemId = R.id.help_nav
            binding.viewPager.setCurrentItem(3, false)
        } else if (tag.equals("Messages", true)) {
            binding.bottomNavigationView.selectedItemId = R.id.messages_nav
            binding.viewPager.setCurrentItem(1, false)
        }
        setupBackPressHandler()
        handleBottomNav()
        askNotificationPermission()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        InAppUpdate.initResult(this, requestCode, resultCode)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        InAppUpdate.initResume(this)
    }

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun handleBottomNav() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_nav -> binding.viewPager.setCurrentItem(0, false)
                R.id.messages_nav -> binding.viewPager.setCurrentItem(1, false)
                R.id.diary_nav -> binding.viewPager.setCurrentItem(2, false)
                R.id.help_nav -> binding.viewPager.setCurrentItem(3, false)
                R.id.profile_nav -> binding.viewPager.setCurrentItem(4, false)
            }
            true
        }
    }

    @SuppressLint("GestureBackNavigation")
    private fun setupBackPressHandler() {

        onBackPressedDispatcher.addCallback(this) {

            val currentItem = binding.viewPager.currentItem

            if (currentItem != 0) {
                changeFragment(0)
            } else {
                if (backPressedOnce) {
                    finishAffinity()
                    return@addCallback
                }

                backPressedOnce = true

                val snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Are you sure you want to exit?",
                    Snackbar.LENGTH_LONG
                ).setAction("Yes") {
                    finishAffinity()
                }

                snackbar.setBackgroundTint(ContextCompat.getColor(this@HomeActivity, R.color.blue1))
                snackbar.setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.white))
                snackbar.setActionTextColor(ContextCompat.getColor(this@HomeActivity, R.color.white))
                snackbar.show()

                Handler(Looper.getMainLooper()).postDelayed({
                    backPressedOnce = false
                }, 2000)
            }
        }
    }

     fun changeFragment(position: Int) {
        binding.viewPager.setCurrentItem(position, false)
        binding.bottomNavigationView.menu.getItem(position).isChecked = true
    }

}