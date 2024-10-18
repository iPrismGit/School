package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.iprism.school.R
import com.iprism.school.databinding.ActivityClassesBinding
import com.iprism.school.databinding.ActivityUpDateClassSubjectsBinding
import com.iprism.school.fragments.ActiveClassesFragment
import com.iprism.school.fragments.InactiveClassFragment

class ClassesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(ActiveClassesFragment())
        handleMoreIv()
        invalidateOptionsMenu()
    }

    private fun handleMoreIv() {
        binding.moreIv.setOnClickListener(View.OnClickListener {
            showPopupMenu(it)
        })
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.class_menu, popup.menu)
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is ActiveClassesFragment) {
            popup.menu.findItem(R.id.active_class_lo).isVisible = false
            popup.menu.findItem(R.id.inactive_class_lo).isVisible = true
        } else if (fragment is InactiveClassFragment) {
            popup.menu.findItem(R.id.active_class_lo).isVisible = true
            popup.menu.findItem(R.id.inactive_class_lo).isVisible = false
        }

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.inactive_class_lo -> {
                    loadFragment(InactiveClassFragment())
                    binding.textView10.text = "Inactive Classes"
                    true
                }

                R.id.active_class_lo -> {
                    loadFragment(ActiveClassesFragment())
                    binding.textView10.text = "Active Classes"
                    true
                }

                else -> false
            }
        }

        popup.show()
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

}