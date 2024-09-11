package com.iprism.school.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.iprism.school.activities.ConsentsActivity
import com.iprism.school.activities.HomeActivity
import com.iprism.school.activities.StaffActivity
import com.iprism.school.activities.StudentsActivity
import com.iprism.school.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        handleStudentsLL()
        handleInboxLL()
        handleMenuImg()
        handleMessageLl()
        handleConsentsLo()
        handleStaffLl()
        return binding.root
    }

    private fun handleConsentsLo() {
        binding.consentsLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, ConsentsActivity::class.java))
        })
    }

    private fun handleStudentsLL() {
        binding.studentsLl.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, StudentsActivity::class.java))
        })
    }

    private fun handleInboxLL() {
        binding.inboxLl.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("tag", "msg")
            startActivity(intent)
        })
    }

    private fun handleMessageLl() {
        binding.messageLl.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("tag", "msg")
            startActivity(intent)
        })
    }

    private fun handleStaffLl() {
        binding.staffLl.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, StaffActivity::class.java)
            startActivity(intent)
        })
    }

    private fun handleMenuImg() {
        binding.menuImg.setOnClickListener(View.OnClickListener {
            if (binding.drawer.isDrawerOpen(Gravity.LEFT)) {
                binding.drawer.closeDrawer(Gravity.LEFT)
            } else {
                binding.drawer.openDrawer(Gravity.LEFT)
            }
        })
    }
}