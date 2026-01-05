package com.iprism.school.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.school.fragments.ActiveStudentsFragment
import com.iprism.school.fragments.InActiveStudentsFragment

class StudentsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ActiveStudentsFragment()
            1 -> InActiveStudentsFragment()
            else -> ActiveStudentsFragment()
        }
    }

}