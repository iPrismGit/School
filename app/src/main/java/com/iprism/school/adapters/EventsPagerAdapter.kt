package com.iprism.school.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.school.fragments.ClassEventsFragment
import com.iprism.school.fragments.DayCareEventsFragment

class EventsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ClassEventsFragment()
            1 -> DayCareEventsFragment()
            else -> ClassEventsFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

}