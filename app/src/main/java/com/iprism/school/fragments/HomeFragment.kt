package com.iprism.school.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.iprism.school.base.BaseFragment
import com.iprism.school.R
import com.iprism.school.activities.AttendanceActivity
import com.iprism.school.activities.CalenderActivity
import com.iprism.school.activities.ConsentsActivity
import com.iprism.school.activities.ContentPagesActivity
import com.iprism.school.activities.HomeActivity
import com.iprism.school.activities.LoginActivity
import com.iprism.school.activities.StudentsActivity
import com.iprism.school.activities.album.AlbumsActivity
import com.iprism.school.activities.album.CreateAlbumsActivity
import com.iprism.school.databinding.FragmentHomeBinding
import com.iprism.school.databinding.ViewMessagesAlertDialogBinding
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.activities.AboutUsActivity
import com.iprism.school.activities.ApplyForLeaveActivity
import com.iprism.school.activities.DayCareAttendanceActivity
import com.iprism.school.activities.DayCarePlansActivity
import com.iprism.school.activities.HolidaysActivity
import com.iprism.school.activities.PlannerCategoriesActivity
import com.iprism.school.activities.StaffAttendanceActivity
import com.iprism.school.activities.album.AlbumDetailsActivity
import com.iprism.school.activities.album.CreateDayCareAlbumsActivity
import com.iprism.school.activities.album.DayCareAlbumDetailsActivity
import com.iprism.school.activities.album.DayCareAlbumsActivity
import com.iprism.school.adapters.HomePAgeDayCareAlbumsAdapter
import com.iprism.school.adapters.HomePageAlbumsAdapter
import com.iprism.school.databinding.AllStudentsPresentBottomSheetBinding
import com.iprism.school.databinding.ClassOrDaycareTypeBottomSheetBinding
import com.iprism.school.interfaces.OnAlbumClickListener
import com.iprism.school.model.classteachermodel.AttendanceStudentsApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.daycare.DayCareStatusApiRequest
import com.iprism.school.model.homepagemodel.AlbumCoverHome
import com.iprism.school.model.homepagemodel.DayCareAlbumCoverHome
import com.iprism.school.model.homepagemodel.HomePageApiRequest
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.DayCareRepository
import com.iprism.school.repositories.HomePageRepository
import com.iprism.school.utils.Constants
import com.iprism.school.utils.UiState
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.DayCareViewModel
import com.iprism.school.viewModels.HomePageViewModel
import com.iprism.school.viewModels.ViewModelFactory

class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var homePageViewModel: HomePageViewModel
    private lateinit var dayCareViewModel: DayCareViewModel
    private lateinit var yesBtn: Button
    private lateinit var noBtn: Button
    private var navigationFrom: String = ""
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var classTypesBinding: ClassOrDaycareTypeBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        Glide.with(this)
            .asGif()
            .load(R.drawable.planner_gif)
            .into(binding.plannerImf)

        val firstName = userDetails?.get(User.FIRST_NAME) ?: ""
        val middleName = userDetails?.get(User.MIDDLE_NAME) ?: ""
        val lastName = userDetails?.get(User.LAST_NAME) ?: ""
        val schoolName = userDetails?.get(User.SCHOOL_NAME) ?: ""
        val image = userDetails?.get(User.IMAGE) ?: ""

        binding.nameTv.text = "Hello $firstName $middleName $lastName"
        binding.staffNameSideTxt.text = "$firstName $middleName $lastName"
        binding.schoolNameTxt.text = schoolName

        if (image.isNotEmpty()) {
            Glide.with(requireContext())
                .load(Constants.IMAGES_URL + image)
                .error(R.drawable.user_img)
                .into(binding.staffProfile)

            Glide.with(requireContext())
                .load(Constants.IMAGES_URL + image)
                .error(R.drawable.user_img)
                .into(binding.profilePic)
        } else {
            binding.staffProfile.setImageResource(R.drawable.user_img)
            binding.profilePic.setImageResource(R.drawable.user_img)
        }

        initViewModel()
        observeAcademicYearsResponse()
        observeDayCareStatusResponse()
        observeHomePageResponse()
        val academicYear = userDetails[User.ACADEMIC_YEAR_ID]?.toString()

        if (academicYear.isNullOrEmpty()) {
            val request = ClassTeacherApiRequest(
                "",
                userDetails[User.ID].toString(),
                userDetails[User.SCHOOL_ID].toString(),
                "",
                "academic_year"
            )
            attendanceViewModel.fetchAcademicYears(request)
        } else {
            var request = HomePageApiRequest(
                userDetails[User.ACADEMIC_YEAR_ID].toString(),
                userDetails[User.SCHOOL_ID].toString(),
                userDetails[User.ID].toString()
            )
            homePageViewModel.fetchHomePageDetails(request)
        }

        handlePlannersAndResorcesLo()
        handleStudentsLL()
        handleInboxLL()
        handleViewAllMessagesLo()
        handleMenuImg()
        handleConsentsLo()
        handleMessageLo()
        handleCalenderLo()
        handleAttendenceLo()
        handleStaffAttendanceLo()
        handleDayCare()
        handleLogoutLo()
        handleAboutusLo()
        handleAlbumsViewAll()
        handleCreateDayCareViewAllLo()
        handleHolidayCalenderLo()
        handleApplyForLeaveLo()
        handleSideHelpTutorialsLo()
        return binding.root
    }

    private fun handleSideHelpTutorialsLo() {
        binding.sideHelpTutorialsLo.setOnClickListener { view ->
            var intent = Intent(requireContext(), HomeActivity::class.java)
            intent.putExtra("tag", "Tutorial")
            startActivity(intent)
        }
    }

    private fun handleApplyForLeaveLo() {
        binding.applyLeaveLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), ApplyForLeaveActivity::class.java))
        }
    }

    private fun handleHolidayCalenderLo() {
        binding.holidayCalenderLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), HolidaysActivity::class.java))
        }
    }

    private fun handleCreateDayCareViewAllLo() {
        binding.dayCareViewAll.setOnClickListener { view ->
            startActivity(Intent(requireContext(), DayCareAlbumsActivity::class.java))
        }
    }

    private fun initViewModel() {
        val repository = AttendanceRepository(requireContext())
        val factory = ViewModelFactory { AttendanceViewModel(repository) }
        attendanceViewModel = ViewModelProvider(this, factory)[AttendanceViewModel::class.java]

        val dayCareRepository = DayCareRepository(requireContext())
        val dayCareFactory = ViewModelFactory { DayCareViewModel(dayCareRepository) }
        dayCareViewModel = ViewModelProvider(this, dayCareFactory)[DayCareViewModel::class.java]

        val homePageRepository = HomePageRepository(requireContext())
        val homePageFactory = ViewModelFactory { HomePageViewModel(homePageRepository) }
        homePageViewModel = ViewModelProvider(this, homePageFactory)[HomePageViewModel::class.java]

    }

    private fun observeAcademicYearsResponse() {
        attendanceViewModel.academicYearsResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    user!!.storeAcademicYear(result.data.id, result.data.name)
                    var request = HomePageApiRequest(
                        userDetails[User.ACADEMIC_YEAR_ID].toString(),
                        userDetails[User.SCHOOL_ID].toString(),
                        userDetails[User.ID].toString()
                    )
                    homePageViewModel.fetchHomePageDetails(request)
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(requireContext(), result.message)
                    Log.d("Message", result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun observeDayCareStatusResponse() {
        dayCareViewModel.dayCareStatusResponse.observe(viewLifecycleOwner) { result ->
            when (result) {

                is UiState.Loading -> binding.progress.showProgress()

                is UiState.Success -> {
                    binding.progress.hideProgress()

                    if (result.data.status.equals("yes", true)) {

                        when (navigationFrom) {

                            "DAYCARE" -> {
                                val intent = Intent(
                                    requireContext(),
                                    DayCarePlansActivity::class.java
                                )
                                intent.putExtra("tag", "DayCare")
                                startActivity(intent)
                            }

                            "ALBUM" -> {
                                val intent = Intent(
                                    requireContext(),
                                    CreateDayCareAlbumsActivity::class.java
                                )
                                startActivity(intent)
                            }

                            "day_care_attendance" -> {
                                val intent = Intent(
                                    requireContext(),
                                    DayCareAttendanceActivity::class.java
                                )
                                startActivity(intent)
                            }
                        }

                        navigationFrom = ""

                    } else {
                        showConfirmationDialog()
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    ToastUtils.showErrorCustomToast(requireContext(), result.message)
                }
            }
        }
    }

    private fun observeHomePageResponse() {
        homePageViewModel.homePageResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.shimmerLo.visibility = View.VISIBLE
                    binding.mainLo.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.shimmerLo.visibility = View.GONE
                    binding.mainLo.visibility = View.VISIBLE
                    var updatedAlbumCoversList = result.data.album_covers.toMutableList()
                    updatedAlbumCoversList.add(0, AlbumCoverHome("", "", "", "-1", "", ""))

                    var updatedDayCareAlbumCoversList =
                        result.data.day_care_album_covers.toMutableList()
                    updatedDayCareAlbumCoversList.add(
                        0,
                        DayCareAlbumCoverHome("", "", "", "-1", "", "")
                    )
                    if (updatedAlbumCoversList.isNotEmpty()) {
                        setupAlbumsAdapter(updatedAlbumCoversList)
                        binding.albumsRv.visibility = View.VISIBLE
                    } else {
                        binding.albumsRv.visibility = View.GONE
                    }

                    if (updatedDayCareAlbumCoversList.isNotEmpty()) {
                        setupDayCareAlbumsAdapter(updatedDayCareAlbumCoversList)
                        binding.dayCareAlbumsRv.visibility = View.VISIBLE
                    } else {
                        binding.dayCareAlbumsRv.visibility = View.GONE
                    }
                }

                is UiState.Error -> {
                    binding.shimmerLo.visibility = View.VISIBLE
                    binding.mainLo.visibility = View.GONE
                    ToastUtils.showErrorCustomToast(requireContext(), result.message)
                    Log.d("Message", result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun setupAlbumsAdapter(albumCovers: List<AlbumCoverHome>) {
        var adapter = HomePageAlbumsAdapter(albumCovers)
        var linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.albumsRv.layoutManager = linearLayoutManager
        binding.albumsRv.adapter = adapter
        adapter.setupListener(object : OnAlbumClickListener {
            override fun onCoverClick(albumId: String, albumName: String) {
                if (albumId.equals("-1", true)) {
                    startActivity(Intent(requireContext(), CreateAlbumsActivity::class.java))
                } else {
                    var intent = Intent(requireContext(), AlbumDetailsActivity::class.java)
                    intent.putExtra("albumId", albumId)
                    intent.putExtra("albumName", albumName)
                    startActivity(intent)
                }
            }

        })
    }

    private fun setupDayCareAlbumsAdapter(albumCovers: List<DayCareAlbumCoverHome>) {
        var adapter = HomePAgeDayCareAlbumsAdapter(albumCovers)
        var linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.dayCareAlbumsRv.layoutManager = linearLayoutManager
        binding.dayCareAlbumsRv.adapter = adapter
        adapter.setupListener(object : OnAlbumClickListener {
            override fun onCoverClick(albumId: String, albumName: String) {

                if (albumId.equals("-1", true)) {

                    navigationFrom = "ALBUM"

                    val request = DayCareStatusApiRequest(
                        userDetails[User.ACADEMIC_YEAR_ID].toString(),
                        userDetails[User.SCHOOL_ID].toString(),
                        userDetails[User.ID].toString()
                    )

                    dayCareViewModel.fetchDayCareStatus(request)

                } else {
                    val intent = Intent(requireContext(), DayCareAlbumDetailsActivity::class.java)
                    intent.putExtra("albumId", albumId)
                    intent.putExtra("albumName", albumName)
                    startActivity(intent)
                }
            }


        })
    }

    private fun handlePlannersAndResorcesLo() {
        binding.plannersAndResourcesLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), PlannerCategoriesActivity::class.java))
        })
    }

    private fun handleViewAllMessagesLo() {
        binding.viewAllMessageLo.setOnClickListener(View.OnClickListener {
            showConfirmationDialog()
        })
    }

    private fun showConfirmationDialog() {
        val binding = ViewMessagesAlertDialogBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.show()

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        binding.okBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun handleAlbumsViewAll() {
        binding.viewAll.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, AlbumsActivity::class.java))
        })
    }

    private fun handleAboutusLo() {
        binding.aboutUsLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, AboutUsActivity::class.java))
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
//            ToastUtils.showSuccessCustomToast(requireContext(), "Clicked On No Button")
            dialog.dismiss()
        })

        yesBtn.setOnClickListener(View.OnClickListener {
            user!!.storeUserDetails(
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
            )

//            ToastUtils.showSuccessCustomToast(requireContext(), "Clicked On Yes Button")
            dialog.dismiss()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finish()
        })
        dialog.show()
    }

    private fun handleDayCare() {
        binding.dayCareLo.setOnClickListener {

            navigationFrom = "DAYCARE"

            val request = DayCareStatusApiRequest(
                userDetails[User.ACADEMIC_YEAR_ID].toString(),
                userDetails[User.SCHOOL_ID].toString(),
                userDetails[User.ID].toString()
            )

            dayCareViewModel.fetchDayCareStatus(request)
        }

    }

    private fun handleStaffAttendanceLo() {
        binding.staffAttendanceLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(requireContext(), StaffAttendanceActivity::class.java))
        })
    }

    private fun handleCalenderLo() {
        binding.calenderLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, CalenderActivity::class.java))
        })
    }

    private fun handleAttendenceLo() {
        binding.attendanceLo.setOnClickListener(View.OnClickListener {
            showAttendanceDayCareBottomSheet()
            // startActivity(Intent(context, AttendanceActivity::class.java))
        })
    }

    private fun handleMessageLo() {
        binding.messagesLo.setOnClickListener(View.OnClickListener {
            binding.allMessagesLo.visibility =
                if (binding.allMessagesLo.isVisible) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
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

    private fun handleMenuImg() {
        binding.menuImg.setOnClickListener(View.OnClickListener {
            binding.drawer.openDrawer(GravityCompat.START)
        })
    }

    private fun showAttendanceDayCareBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        classTypesBinding = ClassOrDaycareTypeBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(classTypesBinding.root)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        classTypesBinding.classTypeRg.setOnCheckedChangeListener { _, checkedId ->
            navigationFrom = when (checkedId) {
                R.id.daycare_rb -> "day_care_attendance"
                R.id.classes_rb -> ""
                else -> ""
            }
        }
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        classTypesBinding.continueBtn.setOnClickListener { view ->
            if (navigationFrom.isEmpty()) {
                startActivity(Intent(context, AttendanceActivity::class.java))
            } else if (navigationFrom.equals("day_care_attendance", true)) {

                val request = DayCareStatusApiRequest(
                    userDetails[User.ACADEMIC_YEAR_ID].toString(),
                    userDetails[User.SCHOOL_ID].toString(),
                    userDetails[User.ID].toString()
                )

                dayCareViewModel.fetchDayCareStatus(request)
            }
            bottomSheetDialog.dismiss()
        }
        classTypesBinding.crossIv.setOnClickListener {

            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

}