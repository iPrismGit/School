package com.iprism.school.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.iprism.school.activities.Messages.MessageActivity
import com.iprism.school.activities.Messages.SentMessagesActivity
import com.iprism.school.activities.StudentsActivity
import com.iprism.school.activities.album.AlbumsActivity
import com.iprism.school.activities.album.CreateAlbumsActivity
import com.iprism.school.databinding.FragmentHomeBinding
import com.iprism.school.databinding.ViewMessagesAlertDialogBinding
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.iprism.school.activities.DayCarePlansActivity
import com.iprism.school.activities.HolidaysActivity
import com.iprism.school.activities.PlannerCategoriesActivity
import com.iprism.school.activities.album.DayCareAlbumsActivity
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.daycare.DayCareStatusApiRequest
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.DayCareRepository
import com.iprism.school.utils.Constants
import com.iprism.school.utils.UiState
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.DayCareViewModel
import com.iprism.school.viewModels.ViewModelFactory

class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var dayCareViewModel: DayCareViewModel
    private lateinit var yesBtn: Button
    private lateinit var noBtn: Button
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

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

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()
        initViewModel()
        observeAcademicYearsResponse()
        observeDayCareStatusResponse()
        var request = ClassTeacherApiRequest(
            "",
            userDetails[User.ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            "",
            "academic_year"
        )
        attendanceViewModel.fetchAcademicYears(request)
        handlePlannersAndResorcesLo()
        handleStudentsLL()
        handleInboxLL()
        handleViewAllMessagesLo()
        handleSentLo()
        handleScheduled()
        handleMenuImg()
        handleMessageLl()
        handleConsentsLo()
        handleMessageLo()
        handleCalenderLo()
        handleAttendenceLo()
        handleStaffAttendanceLo()
        handleDayCare()
        handleLogoutLo()
        handleAboutusLo()
        handleAlbumsViewAll()
        handleCreateAlbumsLo()
        handleCreateDayCareAlbumsLo()
        handleCreateDayCareViewAllLo()
        handleDigitalContentLo()
        handleHolidayCalenderLo()
        return binding.root
    }

    private fun handleHolidayCalenderLo() {
        binding.holidayCalenderLo.setOnClickListener { view ->
            startActivity(Intent(requireContext(), HolidaysActivity::class.java))
        }
    }

    private fun handleDigitalContentLo() {
        binding.staffAttendanceLo.setOnClickListener { view ->
        }
    }

    private fun handleCreateDayCareAlbumsLo() {
       binding.createDayCareLl.setOnClickListener { view ->
           startActivity(Intent(requireContext(), DayCareAlbumsActivity::class.java))
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
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.status.equals("yes", true)) {
                        var intent = Intent(requireContext(), DayCarePlansActivity::class.java)
                        intent.putExtra("tag", "DayCare")
                        startActivity(intent)
                    } else {
                        showConfirmationDialog()
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(requireContext(), result.message)
                    Log.d("Message", result.message)
                    binding.progress.hideProgress()
                }
            }
        }
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

    private fun handleSentLo() {
        binding.sentLo.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, SentMessagesActivity::class.java)
            intent.putExtra("tag", "")
            startActivity(intent)
        })
    }

    private fun handleScheduled() {
        binding.scheduledLo.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, SentMessagesActivity::class.java)
            intent.putExtra("tag", "scheduled")
            startActivity(intent)
        })
    }

    private fun handleAlbumsViewAll() {
        binding.viewAll.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, AlbumsActivity::class.java))
        })
    }

    private fun handleCreateAlbumsLo() {
        binding.createLl.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, AlbumsActivity::class.java))
        })
    }

    private fun handleAboutusLo() {
        binding.aboutUsLo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, ContentPagesActivity::class.java))
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
        binding.dayCareLo.setOnClickListener(View.OnClickListener {
            var request = DayCareStatusApiRequest(
                userDetails[User.ACADEMIC_YEAR_ID].toString(),
                userDetails[User.SCHOOL_ID].toString(), userDetails[User.ID].toString()
            )
            dayCareViewModel.fetchDayCareStatus(request)
        })
    }

    private fun handleStaffAttendanceLo() {
        binding.staffAttendanceLo.setOnClickListener(View.OnClickListener {
            ToastUtils.showErrorCustomToast(
                requireContext(),
                "This Feature is Not Implemented Yet..!"
            )
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

    private fun handleMessageLl() {
        binding.messageLl.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("tag", "msg")
            startActivity(intent)
        })
    }

    private fun handleMenuImg() {
        binding.menuImg.setOnClickListener(View.OnClickListener {
            binding.drawer.openDrawer(GravityCompat.START)
        })
    }

//    private fun allAlbum() {
//        showProgress()
//        var apiRequest = SchoolStaffReq(auth_token,scl_id,teacherId)
//        Log.d("homeUploadAlbum_Req", apiRequest.toString())
//        val call: Call<AlbumsListResponse> = parentApiService!!.albumList(apiRequest)
//        call.enqueue(object : Callback<AlbumsListResponse> {
//            override fun onResponse(call: Call<AlbumsListResponse>, response: Response<AlbumsListResponse>) {
//                if (response.isSuccessful) {
//                    hideProgress()
//                    val loginApiResponse = response.body()
//
//                    if (loginApiResponse!!.status == true){
////                        binding.nodataTv.visibility = View.GONE
//                        binding.albumsRv.visibility = View.VISIBLE
//
//
//                        if (isAdded){
//                            val linearLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
//                            binding.albumsRv.layoutManager = linearLayoutManager
//                            val albumsAdapter = AlbumsAdapter(requireActivity(), loginApiResponse.response.album_details)
//                            binding.albumsRv.adapter = albumsAdapter
//
//                            albumsAdapter.OnItemBtn = {
//                                    mydata ->
//                                val studentId = mydata.id.toString()
//                                val intent = Intent(requireActivity(), AlbumDetailsActivity::class.java)
//                                intent.putExtra("studentId",studentId)
//                                intent.putExtra("albumId",studentId)
//                                startActivity(intent)
//                            }
//
//                        }
//
////                        var albumsAdapter = AlbumsAdapter(requireContext(), loginApiResponse.response.album_details)
////                        binding.albumsRv.adapter = albumsAdapter
////                        var linearLayoutManager = GridLayoutManager(requireContext(), 2)
////                        binding.albumsRv.layoutManager = linearLayoutManager
//
//
//                    }else{
////                        binding.nodataTv.visibility = View.VISIBLE
//                        binding.albumsRv.visibility = View.GONE
//                    }
//                } else {
////                    binding.nodataTv.visibility = View.VISIBLE
//                    binding.albumsRv.visibility = View.GONE
//
//                    hideProgress()
//                    ToastUtils.showErrorCustomToast(requireActivity(), response.message())
//                }
//            }
//            override fun onFailure(call: Call<AlbumsListResponse>, t: Throwable) {
//                hideProgress()
////                ToastUtils.showErrorCustomToast(requireActivity(), t.message.toString())
//            }
//        })
//    }

}