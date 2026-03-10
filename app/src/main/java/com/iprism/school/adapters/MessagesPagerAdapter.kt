package com.iprism.school.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.school.fragments.ClassMessagesFragment
import com.iprism.school.fragments.DayCareMessagesFragment

class MessagesPagerAdapter(activity: Fragment) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ClassMessagesFragment()
            1 -> DayCareMessagesFragment()
            else -> ClassMessagesFragment()
        }
    }

    override fun getItemCount(): Int {
       return 2
    }

}