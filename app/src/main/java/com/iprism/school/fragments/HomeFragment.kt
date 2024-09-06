package com.iprism.school.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.iprism.school.activities.StudentsActivity
import com.iprism.school.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        handleStudentsLL()
        binding.menuImg.setOnClickListener(View.OnClickListener {
            if (binding.drawer.isDrawerOpen(Gravity.LEFT)) {
                binding.drawer.closeDrawer(Gravity.LEFT)
            } else {
                binding.drawer.openDrawer(Gravity.LEFT)
            }
        })
        return binding.root
    }

    private fun handleStudentsLL() {
        binding.studentsLl.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, StudentsActivity::class.java))
        })
    }
}