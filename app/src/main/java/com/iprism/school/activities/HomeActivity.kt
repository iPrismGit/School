package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.iprism.school.R
import com.iprism.school.databinding.ActivityHomeBinding
import com.iprism.school.fragments.HomeFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBottomNav()
        switchFragment(HomeFragment())
    }

    private fun handleBottomNav() {
        binding.bottomNavigationView.setOnItemSelectedListener() { item ->
            when(item.itemId) {
                R.id.home -> {
                    switchFragment(HomeFragment())
                    true
                }
                R.id.messages -> {
                    // Respond to navigation item 2 click
                    true
                }R.id.messages -> {
                // Respond to navigation item 2 click
                true
                }R.id.messages -> {
                // Respond to navigation item 2 click
                true
                }R.id.messages -> {
                // Respond to navigation item 2 click
                true
                }
                else -> false
            }
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.flFragment, fragment)
            .commit();
    }
}