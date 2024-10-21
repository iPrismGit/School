package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityClassesBinding
import com.iprism.school.databinding.ActivityUpDateClassSubjectsBinding
import com.iprism.school.databinding.AddMoreBottomSheetLayoutBinding
import com.iprism.school.databinding.FilterBottomSheetBinding
import com.iprism.school.fragments.ActiveClassesFragment
import com.iprism.school.fragments.InactiveClassFragment
import com.iprism.school.utils.ToastUtils

class ClassesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(ActiveClassesFragment())
        handleMoreIv()
        invalidateOptionsMenu()
        handleFilterIv()
    }

    private fun handleFilterIv() {
        binding.filterImg.setOnClickListener(View.OnClickListener {
            showFiltersBottomSheet()
        })
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

                R.id.class_transfer_lo -> {
                    startActivity(Intent(this, ClassTransferActivity::class.java))
                    true
                }

                R.id.add_session_lo -> {
                    startActivity(Intent(this, SessionsActivity::class.java))
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

    private fun showFiltersBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val filterBinding = FilterBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(filterBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            filterBinding.applyBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "Filters Applied..")
            })

            filterBinding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            filterBinding.removeFilterBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

}