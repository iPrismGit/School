package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.iprism.school.R
import com.iprism.school.databinding.ActivityHomeBinding
import com.iprism.school.fragments.ChildCareFragment
import com.iprism.school.fragments.HomeFragment
import com.iprism.school.fragments.MessagesFragment
import com.iprism.school.fragments.ScannerFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var tag: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBottomNav()
        if (intent.hasExtra("tag")) {
            tag = intent.getStringExtra("tag").toString()
        }
        if (tag.equals("msg", true)) {
            binding.bottomNavigationView.selectedItemId = R.id.messages
            switchFragment(MessagesFragment())
        } else if (tag.equals("Dairy", true) || tag.equals("DayCare", true)) {
            binding.bottomNavigationView.selectedItemId = R.id.childcare
            val childFragment = ChildCareFragment()
            val bundle = Bundle()
            bundle.putString("tag", tag) // Pass your value here
            childFragment.arguments = bundle
            switchFragment(childFragment)
            true
        } else {
            binding.bottomNavigationView.selectedItemId = R.id.home
            switchFragment(HomeFragment())
        }
    }

    private fun handleBottomNav() {
        binding.bottomNavigationView.setOnItemSelectedListener() { item ->
            when (item.itemId) {
                R.id.home -> {
                    switchFragment(HomeFragment())
                    true
                }

                R.id.messages -> {
                    switchFragment(MessagesFragment())
                    true
                }

                R.id.scanner -> {
                    switchFragment(ScannerFragment())
                    true
                }

                R.id.childcare -> {
                    val childFragment = ChildCareFragment()
                    val bundle = Bundle()
                    bundle.putString("tag", "Dairy") // Pass your value here
                    childFragment.arguments = bundle
                    switchFragment(childFragment)
                    true
                }

                R.id.help -> {
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