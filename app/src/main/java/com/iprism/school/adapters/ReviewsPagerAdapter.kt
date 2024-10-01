package com.iprism.school.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.school.fragments.ActiveConsentsFragment
import com.iprism.school.fragments.InactiveConsentsFragment

class ReviewsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ActiveConsentsFragment()
            1 -> InactiveConsentsFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}