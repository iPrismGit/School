package com.iprism.school.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.school.fragments.ActiveConsentsFragment
import com.iprism.school.fragments.InactiveConsentsFragment
import com.iprism.school.fragments.StaffActiveFragment
import com.iprism.school.fragments.StaffInActiveFragment

class StaffTabsAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StaffActiveFragment()
            1 -> StaffInActiveFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}