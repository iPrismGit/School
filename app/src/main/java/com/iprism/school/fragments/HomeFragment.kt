package com.iprism.school.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.iprism.school.activities.AttendanceActivity
import com.iprism.school.activities.CalenderActivity
import com.iprism.school.activities.ConsentsActivity
import com.iprism.school.activities.HomeActivity
import com.iprism.school.activities.MealPlannerActivity
import com.iprism.school.activities.StaffAttendanceActivity
import com.iprism.school.activities.StudentsActivity
import com.iprism.school.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        handleStudentsLL()
        handleInboxLL()
        handleMenuImg()
        handleMealPlanningLo()
        handleMessageLl()
        handleConsentsLo()
        handleMessageLo()
        handleDirectoriesLo()
        handleFeeLo()
        handleStaffAttendenceLo()
        handleCalenderLo()
        handleAttendenceLo()
        handleDairy()
        handleDayCare()
        return binding.root
    }

    private fun handleDayCare() {
        binding.dayCareLo.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("tag", "DayCare")
            startActivity(intent)
        })
    }

    private fun handleDairy() {
        binding.dairyLo.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("tag", "Dairy")
            startActivity(intent)
        })
    }

    private fun handleCalenderLo() {
        binding.calenderLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, CalenderActivity::class.java))
        })
    }

    private fun handleAttendenceLo() {
        binding.attendanceLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, AttendanceActivity::class.java))
        })
    }

    private fun handleFeeLo() {
        binding.feeLo.setOnClickListener(View.OnClickListener {
            binding.allFeesesLo.visibility = View.VISIBLE
        })
    }

    private fun handleDirectoriesLo() {
        binding.directoryLo.setOnClickListener(View.OnClickListener {
            binding.allDirectoriesLo.visibility = View.VISIBLE
        })
    }

    private fun handleMessageLo() {
        binding.messagesLo.setOnClickListener(View.OnClickListener {
            binding.allMessagesLo.visibility = View.VISIBLE
        })
    }

    private fun handleStaffAttendenceLo() {
        binding.staffAtendenceLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, StaffAttendanceActivity::class.java))
        })
    }

    private fun handleMealPlanningLo() {
        binding.mealPlannerLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, MealPlannerActivity::class.java))
        })
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