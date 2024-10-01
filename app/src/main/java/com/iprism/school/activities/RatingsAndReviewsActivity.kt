package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.core.widget.NestedScrollView
import com.google.android.material.tabs.TabLayoutMediator
import com.iprism.school.R
import com.iprism.school.adapters.ConsentsTabsAdapter
import com.iprism.school.adapters.ReviewsPagerAdapter
import com.iprism.school.databinding.ActivityRatingsAndReviewsBinding

class RatingsAndReviewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRatingsAndReviewsBinding
    private var averageValue: Int = 0
    private var infrastructrureValue: Int = 4
    private var securityValue: Int = 3
    private var curriculumAndActivitiesValue: Int = 2
    private var communicationValue: Int = 5
    private var staffValue: Int = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingsAndReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpTabs()
        setSelectedRating(
            infrastructrureValue,
            binding.infrastructure1Rb,
            binding.infrastructure2Rb,
            binding.infrastructure3Rb,
            binding.infrastructure4Rb,
            binding.infrastructure5Rb
        )
        setSelectedRating(
            securityValue,
            binding.security1Rb,
            binding.security2Rb,
            binding.security3Rb,
            binding.security4Rb,
            binding.security5Rb
        )
        setSelectedRating(
            curriculumAndActivitiesValue,
            binding.curriculumAndActivities1Rb,
            binding.curriculumAndActivities2Rb,
            binding.curriculumAndActivities3Rb,
            binding.curriculumAndActivities4Rb,
            binding.curriculumAndActivities5Rb
        )
        setSelectedRating(
            communicationValue,
            binding.communication1Rb,
            binding.communication2Rb,
            binding.communication3Rb,
            binding.communication4Rb,
            binding.communication5Rb
        )
        setSelectedRating(
            staffValue,
            binding.staff1Rb,
            binding.staff2Rb,
            binding.staff3Rb,
            binding.staff4Rb,
            binding.staff5Rb
        )
        setSelectedRating(
            18 / 5,
            binding.average1Rb,
            binding.average2Rb,
            binding.average3Rb,
            binding.average4Rb,
            binding.average5Rb
        )
    }

    private fun setUpTabs() {
        val reviewsPagerAdapter = ReviewsPagerAdapter(this)
        binding.viewPager.adapter = reviewsPagerAdapter
        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Pending"
                    1 -> tab.text = "Approved"
                    else -> tab.text = "Rejected"
                }
            }
        tabLayoutMediator.attach()
    }

    private fun setSelectedRating(
        ratingValue: Int,
        vararg radioButtons: RadioButton
    ) {

        radioButtons.forEachIndexed { index, radioButton ->
            radioButton.isChecked = index < ratingValue
            radioButton.isClickable = false
        }
    }

}