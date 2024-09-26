package com.iprism.school.fragments

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.iprism.school.R
import com.iprism.school.activities.CreatedDiaryActivity
import com.iprism.school.activities.DaycareEmailReportActivity
import com.iprism.school.activities.DaycareReportActivity
import com.iprism.school.activities.SetActivityIconActivity
import com.iprism.school.adapters.PagerAdapter
import com.iprism.school.databinding.FragmentChildCareBinding
import com.iprism.school.utils.ToastUtils

class ChildCareFragment : Fragment() {

    private lateinit var binding: FragmentChildCareBinding
    private var tag: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildCareBinding.inflate(layoutInflater)
        tag = arguments?.getString("tag").toString()
        Log.d("tagFragment", tag)
        setupTabs()
        hanldeSaveBtn()
        if (tag != null) {
            binding.viewPager.currentItem = if (tag == "Dairy") 0 else 1
        }

        if (tag.equals("Dairy", true)) {
            binding.saveBtn.visibility = View.VISIBLE
            binding.dayCareLo.visibility = View.GONE
        } else {
            binding.dayCareLo.visibility = View.VISIBLE
            binding.saveBtn.visibility = View.GONE
        }
        hanldeSaveBtn()
        handleCalenderBtn()
        handleThreeDots()
        handleEmailBtn()
        return binding.root
    }

    private fun handleEmailBtn() {
        binding.emailIv.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, DaycareEmailReportActivity::class.java))
        })
    }

    private fun handleThreeDots() {
        binding.moreIv.setOnClickListener(View.OnClickListener {
            showPopupMenu(it)
        })
    }

    private fun handleCalenderBtn() {
        binding.calenderIv.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, DaycareReportActivity::class.java))
        })
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.side_rigth_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.change_icon -> {
                    startActivity(Intent(context, SetActivityIconActivity::class.java))
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
    private fun hanldeSaveBtn() {
        binding.saveBtn.setOnClickListener(View.OnClickListener {
            blinkButton(binding.saveBtn)
            startActivity(Intent(context, CreatedDiaryActivity::class.java))
            ToastUtils.showSuccessCustomToast( requireContext(),"Daily Report Created Successfully")
        })
    }

    private fun blinkButton(button: Button) {
        // ObjectAnimator to animate the button's alpha (opacity)
        val blinkAnimation = ObjectAnimator.ofFloat(button, "alpha", 0f, 1f)
        blinkAnimation.duration = 500 // 500ms for the blink
        blinkAnimation.repeatCount = 0 // Blink twice
        blinkAnimation.start()
    }

    private fun setupTabs() {
        val pagerAdapter = PagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Dairy"
                    1 -> tab.text = "Day Care"
                    else -> tab.text = "Day Care"
                }
            }
        tabLayoutMediator.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> { // Dairy Tab
                        binding.saveBtn.visibility = View.VISIBLE
                        binding.dayCareLo.visibility = View.GONE
                    }

                    1 -> { // Day Care Tab
                        binding.saveBtn.visibility = View.GONE
                        binding.dayCareLo.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

}