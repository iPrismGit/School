package com.iprism.school.fragments

import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.iprism.school.R
import com.iprism.school.adapters.PagerAdapter
import com.iprism.school.databinding.FragmentChildCareBinding

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
        return binding.root
    }

    private fun hanldeSaveBtn() {
        binding.saveBtn.setOnClickListener(View.OnClickListener {
            blinkButton(binding.saveBtn)
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