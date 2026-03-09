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
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
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
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.activities.AboutUsActivity
import com.iprism.school.activities.ApplyForLeaveActivity
import com.iprism.school.activities.ChatActivity
import com.iprism.school.activities.DayCareAttendanceActivity
import com.iprism.school.activities.DayCarePlansActivity
import com.iprism.school.activities.HolidaysActivity
import com.iprism.school.activities.PlannerCategoriesActivity
import com.iprism.school.activities.SchoolSupportActivity
import com.iprism.school.activities.StaffAttendanceActivity
import com.iprism.school.activities.StudentLeaveRequestsActivity
import com.iprism.school.activities.TechnicalSupportActivity
import com.iprism.school.activities.album.AlbumDetailsActivity
import com.iprism.school.activities.album.CreateDayCareAlbumsActivity
import com.iprism.school.activities.album.DayCareAlbumDetailsActivity
import com.iprism.school.activities.album.DayCareAlbumsActivity
import com.iprism.school.adapters.HomePAgeDayCareAlbumsAdapter
import com.iprism.school.adapters.HomePageAlbumsAdapter
import com.iprism.school.adapters.MessagesAdapter
import com.iprism.school.databinding.AllStudentsPresentBottomSheetBinding
import com.iprism.school.databinding.ClassOrDaycareTypeBottomSheetBinding
import com.iprism.school.interfaces.OnAlbumClickListener
import com.iprism.school.interfaces.OnMessageClickListener
import com.iprism.school.model.classteachermodel.AttendanceStudentsApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.daycare.DayCareStatusApiRequest
import com.iprism.school.model.homepagemodel.AlbumCoverHome
import com.iprism.school.model.homepagemodel.DayCareAlbumCoverHome
import com.iprism.school.model.homepagemodel.HomePageApiRequest
import com.iprism.school.model.leaverequestmodel.LeaveRequestApiRequest
import com.iprism.school.model.messagemodel.MessageThread
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.DayCareRepository
import com.iprism.school.repositories.HomePageRepository
import com.iprism.school.repositories.LeaveRequestRepository
import com.iprism.school.utils.Constants
import com.iprism.school.utils.UiState
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.DayCareViewModel
import com.iprism.school.viewModels.HomePageViewModel
import com.iprism.school.viewModels.LeaveRequestsViewModel
import com.iprism.school.viewModels.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    private var leaveRequestCount = 0
    private lateinit var leaveRequestViewModel: LeaveRequestsViewModel

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
        binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        binding.drawer.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerStateChanged(newState: Int) {
                if (newState == DrawerLayout.STATE_IDLE) {
                    binding.drawer.requestLayout()
                }
            }
        })
        binding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.blue1)
        )

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
            Log.d("HomePageRequest", request.toString())
            homePageViewModel.fetchHomePageDetails(request)
        }

        handlePlannersAndResorcesLo()
        handleStudentsLL()
        handleMenuImg()
        handleConsentsLo()
        handleMessageLo()
        handleSideMessageLo()
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
        handleSchoolSupportLo()
        handleTechnicalSupportLo()
        handleLeaveRequestsLo()
        observeLeaveRequestCountResponse()
        fetchLeaveRequestCount()
        handleViewAllMessages()
        handleDigitalContentLo()
        refresh()
        handleTimeTableLo()
        return binding.root
    }

    private fun handleTimeTableLo() {
        binding.timeTableLo.setOnClickListener { view ->
            ToastUtils.showErrorCustomToast(requireContext(), "This Feature is no Longer Visible..!")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refresh() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                var request = HomePageApiRequest(
                    userDetails[User.ACADEMIC_YEAR_ID].toString(),
                    userDetails[User.SCHOOL_ID].toString(),
                    userDetails[User.ID].toString()
                )
                Log.d("HomePageRequest", request.toString())
                homePageViewModel.fetchHomePageDetails(request)
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun handleDigitalContentLo() {
        binding.degitalContentLo.setOnClickListener { view ->
            var intent = Intent(requireContext(), HomeActivity::class.java)
            intent.putExtra("tag", "Tutorial")
            startActivity(intent)
        }
    }

    private fun handleViewAllMessages() {
        binding.messagesViewAll.setOnClickListener { view ->
            var intent = Intent(requireContext(), HomeActivity::class.java)
            intent.putExtra("tag", "Messages")
            startActivity(intent)
        }
    }

    private fun fetchLeaveRequestCount() {
        lifecycleScope.launch {
            while (true) {
                getNotificationCountFromApi()
                delay(4000)
            }
        }
    }

    private fun getNotificationCountFromApi() {
        var leaveRequestApiRequest = LeaveRequestApiRequest(
            userDetails[User.SCHOOL_ID].toString(),
            "",
            "",
            "",
            "",
            userDetails[User.ID].toString(),
            "count",
            ""
        )
        Log.d("LeaveRequestsRequest", leaveRequestApiRequest.toString())
        leaveRequestViewModel.fetchLeaveRequestsCount(leaveRequestApiRequest)
    }

    private fun observeLeaveRequestCountResponse() {
        leaveRequestViewModel.leaveRequestsCountResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {

                }

                is UiState.Success -> {
                    leaveRequestCount = result.data.response.count
                    updateBadge(leaveRequestCount)
                    Log.d("notificationCount", leaveRequestCount.toString())
                }

                is UiState.Error -> {
                    //  ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
    }

    private fun updateBadge(count: Int) {
        leaveRequestCount = count
        if (count > 0) {
            if (count > 10) {
                binding.countTxt.text = "10+"
            } else {
                binding.countTxt.text = count.toString()
            }
            binding.countLo.visibility = View.VISIBLE

        } else {
            binding.countLo.visibility = View.GONE
        }
    }

    private fun handleLeaveRequestsLo() {
        binding.leaveRequestsLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), StudentLeaveRequestsActivity::class.java))
        }
    }

    private fun handleTechnicalSupportLo() {
        binding.technicalSupportLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), TechnicalSupportActivity::class.java))
        }
    }

    private fun handleSchoolSupportLo() {
        binding.schoolSupportLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), SchoolSupportActivity::class.java))
        }
    }

    private fun handleMessageLo() {
        binding.messageLl.setOnClickListener { view ->
            var intent = Intent(requireContext(), HomeActivity::class.java)
            intent.putExtra("tag", "Messages")
            startActivity(intent)
        }
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

        val leaveRequestsRepository = LeaveRequestRepository(requireContext())
        val leaveRequestFactory =
            ViewModelFactory { LeaveRequestsViewModel(leaveRequestsRepository) }
        leaveRequestViewModel =
            ViewModelProvider(this, leaveRequestFactory)[LeaveRequestsViewModel::class.java]

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

                    if (result.data.messages.isNotEmpty()) {
                        binding.messagesListLo.visibility = View.VISIBLE
                        setupMessagesAdapter(result.data.messages as ArrayList<MessageThread?>)
                    } else {
                        binding.messagesListLo.visibility = View.GONE
                    }
                }

                is UiState.Error -> {
                    binding.shimmerLo.visibility = View.VISIBLE
                    binding.mainLo.visibility = View.GONE
                    ToastUtils.showErrorCustomToast(requireContext(), result.message)
                    if (result.message.equals("You are marked as ex-staff", true)) {
                        user?.logoutUser()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                    }
                    Log.d("Message", result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun setupMessagesAdapter(messages: ArrayList<MessageThread?>) {
        var adapter = MessagesAdapter(messages)
        var linearLayoutManager =
            LinearLayoutManager(requireContext())
        binding.messagesRv.layoutManager = linearLayoutManager
        binding.messagesRv.adapter = adapter
        adapter.setupListener(object : OnMessageClickListener {
            override fun onItemClick(
                threadId: String,
                name: String,
                image: String,
                type: String,
                studentId: String
            ) {
                var intent = Intent(requireContext(), ChatActivity::class.java)
                intent.putExtra("threadId", threadId)
                intent.putExtra("name", name)
                intent.putExtra("image", image)
                intent.putExtra("messageType", type)
                intent.putExtra("studentId", studentId)
                startActivity(intent)
            }

            override fun onStudentSelectClick(
                value: String,
                studentId: String,
                studentName: String
            ) {
            }

            override fun onInnerItemClick(eventImage: String) {
            }

        })
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
            user?.logoutUser()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
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

    private fun handleSideMessageLo() {
        binding.messagesLo.setOnClickListener(View.OnClickListener {
            var intent = Intent(requireContext(), HomeActivity::class.java)
            intent.putExtra("tag", "Messages")
            startActivity(intent)
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

    private fun handleMenuImg() {
        binding.menuImg.setOnClickListener {

            if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
                binding.drawer.closeDrawer(GravityCompat.START)
            } else {
                binding.drawer.post {
                    binding.drawer.openDrawer(GravityCompat.START)
                }
            }
        }
    }

    private fun showAttendanceDayCareBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        classTypesBinding = ClassOrDaycareTypeBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(classTypesBinding.root)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        classTypesBinding.classTypeRg.setOnCheckedChangeListener { _, checkedId ->
            navigationFrom = when (checkedId) {
                R.id.daycare_rb -> "day_care_attendance"
                R.id.classes_rb -> "classes"
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
                ToastUtils.showErrorCustomToast(requireContext(), "Please Select Class or Day Care")
            } else if (navigationFrom.equals("day_care_attendance", true)) {
                val request = DayCareStatusApiRequest(
                    userDetails[User.ACADEMIC_YEAR_ID].toString(),
                    userDetails[User.SCHOOL_ID].toString(),
                    userDetails[User.ID].toString()
                )
                dayCareViewModel.fetchDayCareStatus(request)
                bottomSheetDialog.dismiss()
            } else if (navigationFrom.equals("classes", true)) {
                startActivity(Intent(context, AttendanceActivity::class.java))
                bottomSheetDialog.dismiss()
            }

            navigationFrom = ""

        }
        classTypesBinding.crossIv.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

}