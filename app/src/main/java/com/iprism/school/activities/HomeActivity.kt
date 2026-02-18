package com.iprism.school.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.adapters.HomePagerAdapter
import com.iprism.school.databinding.ActivityHomeBinding


class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var tag = ""
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        tag = intent.getStringExtra("tag").toString()
        val adapter = HomePagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter
        if (tag.isEmpty()){
            binding.bottomNavigationView.selectedItemId = R.id.home_nav
            binding.viewPager.setCurrentItem(0, false)
        } else if (tag.equals("Tutorial", true)){
            binding.bottomNavigationView.selectedItemId = R.id.help_nav
            binding.viewPager.setCurrentItem(3, false)
        } else if (tag.equals("Messages", true)){
            binding.bottomNavigationView.selectedItemId = R.id.messages_nav
            binding.viewPager.setCurrentItem(1, false)
        }

        handleBottomNav()
    }

    private fun handleBottomNav() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_nav -> binding.viewPager.setCurrentItem(0, false)
                R.id.messages_nav -> binding.viewPager.setCurrentItem(1, false)
                R.id.diary_nav -> binding.viewPager.setCurrentItem(2, false)
                R.id.help_nav -> binding.viewPager.setCurrentItem(3, false)
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