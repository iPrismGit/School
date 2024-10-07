package com.iprism.school.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iprism.school.fragments.CabScannerFragment
import com.iprism.school.fragments.DayCareFragment
import com.iprism.school.fragments.DiaryFragment
import com.iprism.school.fragments.StaffScannerFragment
import com.iprism.school.fragments.StudentScannerFragment

class ScannerTabsAdapter (fragment: Fragment) : FragmentStateAdapter(fragment)  {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StaffScannerFragment()
            1 -> StudentScannerFragment()
            2 -> CabScannerFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

}