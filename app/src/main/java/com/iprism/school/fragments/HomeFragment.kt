package com.iprism.school.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.iprism.school.R
import com.iprism.school.activities.AttendanceActivity
import com.iprism.school.activities.CalenderActivity
import com.iprism.school.activities.ConsentsActivity
import com.iprism.school.activities.FeedBackActivity
import com.iprism.school.activities.GroupsActivity
import com.iprism.school.activities.HomeActivity
import com.iprism.school.activities.StaffActivity
import com.iprism.school.activities.LoginActivity
import com.iprism.school.activities.MealPlannerActivity
import com.iprism.school.activities.PromotionsActivity
import com.iprism.school.activities.RatingsAndReviewsActivity
import com.iprism.school.activities.StaffAttendanceActivity
import com.iprism.school.activities.StudentsActivity
import com.iprism.school.databinding.FragmentHomeBinding
import com.iprism.school.utils.ToastUtils

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var yesBtn :Button
    private lateinit var noBtn :Button

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
        handleStaffLl()
        handleMessageLo()
        handleDirectoriesLo()
        handleFeeLo()
        handleStaffAttendenceLo()
        handleCalenderLo()
        handleAttendenceLo()
        handleDairy()
        handleDayCare()
        handleReviewsAndRatingsLo()
        handlePromotionsLo()
        handleSuggestionsLo()
        handleLogoutLo()
        hnaldestudentsLo()
        handleGroupsLo()
        return binding.root
    }

    private fun handleGroupsLo() {
        binding.groupsLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, GroupsActivity::class.java))
        })
    }

    private fun hnaldestudentsLo() {
        binding.studentsLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, StudentsActivity::class.java))
        })
    }

    private fun handleLogoutLo() {
        binding.logoutLo.setOnClickListener(View.OnClickListener {
            showLogoutDialog()
        })
    }

    @SuppressLint("MissingInflatedId")
    private fun showLogoutDialog() {
        val dialogView = layoutInflater.inflate(R.layout.log_out_dialog, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        yesBtn = dialogView.findViewById(R.id.yes_btn) as Button
        noBtn = dialogView.findViewById(R.id.no_btn) as Button
        noBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(requireContext(), "Clicked On No Button")
            dialog.dismiss()
        })

        yesBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(requireContext(), "Clicked On Yes Button")
            dialog.dismiss()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        })
        dialog.show()
    }

    private fun handleSuggestionsLo() {
        binding.sendSuggestionsLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), FeedBackActivity::class.java))
        })
    }

    private fun handlePromotionsLo() {
        binding.promotionsLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), PromotionsActivity::class.java))
        })
    }

    private fun handleReviewsAndRatingsLo() {
        binding.reviewsAndRatingsLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), RatingsAndReviewsActivity::class.java))
        })
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