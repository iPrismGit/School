package com.iprism.school.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.school.fragments.ActiveConsentsFragment
import com.iprism.school.fragments.ApprovedReviewsFragment
import com.iprism.school.fragments.InactiveConsentsFragment
import com.iprism.school.fragments.PendingReviewsFragment
import com.iprism.school.fragments.RejectedReviewsFragment

class ReviewsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PendingReviewsFragment()
            1 -> ApprovedReviewsFragment()
            2 -> RejectedReviewsFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

}