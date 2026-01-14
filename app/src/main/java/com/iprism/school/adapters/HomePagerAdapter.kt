package com.iprism.school.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.school.fragments.DayCareFragment
import com.iprism.school.fragments.DiaryFragment
import com.iprism.school.fragments.HelpTutorialsFragment
import com.iprism.school.fragments.HomeFragment
import com.iprism.school.fragments.MessagesFragment
import com.iprism.school.fragments.ScannerFragment

class HomePagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> MessagesFragment()
            2 -> ScannerFragment()
            3 -> DiaryFragment()
            4 -> HelpTutorialsFragment()
            else -> HomeFragment()
        }
    }

    override fun getItemCount(): Int {
        return 5
    }
}