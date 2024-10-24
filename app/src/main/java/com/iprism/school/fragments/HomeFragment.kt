package com.iprism.school.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.iprism.school.R
import com.iprism.school.activities.AlbumsActivity
import com.iprism.school.activities.AttendanceActivity
import com.iprism.school.activities.CalenderActivity
import com.iprism.school.activities.ChildHandOverActivity
import com.iprism.school.activities.ClassesActivity
import com.iprism.school.activities.ConsentsActivity
import com.iprism.school.activities.ContentPagesActivity
import com.iprism.school.activities.FeedBackActivity
import com.iprism.school.activities.GroupsActivity
import com.iprism.school.activities.HomeActivity
import com.iprism.school.activities.InviteParentsActivity
import com.iprism.school.activities.StaffActivity
import com.iprism.school.activities.LoginActivity
import com.iprism.school.activities.MealPlannerActivity
import com.iprism.school.activities.MessageActivity
import com.iprism.school.activities.PromotionsActivity
import com.iprism.school.activities.RatingsAndReviewsActivity
import com.iprism.school.activities.StaffAttendanceActivity
import com.iprism.school.activities.StudentsActivity
import com.iprism.school.activities.SubjectsActivity
import com.iprism.school.databinding.FragmentHomeBinding
import com.iprism.school.utils.ToastUtils

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var yesBtn: Button
    private lateinit var noBtn: Button
    private val CAMERA_REQUEST_CODE = 100
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            openDisplayImageActivity(imageBitmap)
        } else {
            Toast.makeText(requireContext(), "Camera capture failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        handleStudentsLL()
        handleInboxLL()
        handleSentLo()
        handleScheduled()
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
        handleSubjectsLo()
        handleClassLo()
        handleAboutusLo()
        handleInviteParentsLo()
        hnaldeAlbumsViewAll()
        handleChildHandoverLo()
        return binding.root
    }

    private fun handleSentLo() {
        binding.sentLo.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("tag", "sent")
            startActivity(intent)
        })
    }

    private fun handleScheduled() {
        binding.scheduledLo.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("tag", "scheduled")
            startActivity(intent)
        })
    }

    private fun handleChildHandoverLo() {
        binding.childHandoverLo.setOnClickListener(View.OnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, open the camera
                openCamera()
            } else {
                // Request camera permission
                requestCameraPermission()
            }
        })
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            cameraLauncher.launch(cameraIntent)
        } else {
            Toast.makeText(requireContext(), "No Camera App Found", Toast.LENGTH_SHORT).show()
        }
    }

    // Request camera permission
    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // Register a permission request launcher
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, open the camera
            openCamera()
        } else {
            Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    // Method to open DisplayImageActivity and pass the image
    private fun openDisplayImageActivity(imageBitmap: Bitmap) {
        val intent = Intent(requireContext(), ChildHandOverActivity::class.java)
        intent.putExtra("capturedImage", imageBitmap)
        startActivity(intent)
    }
    private fun hnaldeAlbumsViewAll() {
        binding.albumsViewAllLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, AlbumsActivity::class.java))
        })
    }

    private fun handleInviteParentsLo() {
        binding.inviteParentsLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, InviteParentsActivity::class.java))
        })
    }

    private fun handleAboutusLo() {
        binding.aboutUsLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, ContentPagesActivity::class.java))
        })
    }

    private fun handleClassLo() {
        binding.classesLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, ClassesActivity::class.java))
        })
    }

    private fun handleSubjectsLo() {
        binding.subjectsLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, SubjectsActivity::class.java))
        })
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
            intent.putExtra("tag", "msgInbox")
            startActivity(intent)
        })
    }

    private fun handleMessageLl() {
        binding.messageLl.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, MessageActivity::class.java)
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