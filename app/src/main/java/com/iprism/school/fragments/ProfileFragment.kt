package com.iprism.school.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.iprism.school.R
import com.iprism.school.base.BaseFragment
import com.iprism.school.databinding.FragmentProfileBinding
import com.iprism.school.model.helptutorials.HelpTutorialsApiRequest
import com.iprism.school.model.profile.ProfileApiRequest
import com.iprism.school.repositories.HelpTutorialsRepository
import com.iprism.school.repositories.ProfileRepository
import com.iprism.school.utils.Constants
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.HelpTutorialsViewModel
import com.iprism.school.viewModels.ProfileViewModel
import com.iprism.school.viewModels.ViewModelFactory

class ProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        initViewModel()
        setupObservers()
        fetchProfileDetails()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        viewModel.profileResponse.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.mainLo.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.mainLo.visibility = View.VISIBLE
                    binding.progress.hideProgress()
                    user!!.storeNewUserDetails(
                        state.data.response.id,
                        state.data.response.first_name,
                        state.data.response.middle_name,
                        state.data.response.last_name,
                        userDetails[User.SCHOOL_ID],
                        state.data.response.mobile,
                        userDetails[User.SCHOOL_NAME],
                        state.data.response.image
                    )
                    binding.tvName.text =
                        state.data.response.first_name + " " + state.data.response.middle_name + " " + state.data.response.last_name
                    binding.rollTxt.text = state.data.response.job_title
                    binding.branchNameTxt.text = state.data.response.branch
                    binding.experienceTxt.text =
                        "Experience : " + state.data.response.exp + " Years"
                    binding.dateOfJoiningTxt.text = "Date of Joining : " + state.data.response.doj
                    binding.dateOfBirthTxt.text = "Date of Birth : " + state.data.response.dob
                    binding.phoneTxt.text = "Phone Number : " + state.data.response.mobile
                    binding.emailTxt.text = "Email  : " + state.data.response.email
                    binding.addressTxt.text =
                        "Address : " + state.data.response.permanent_address
                    var gender = state.data.response.gender
                    if (gender.equals("1", true)) {
                        gender = "Male"
                    } else if (gender.equals("2", true)) {
                        gender = "Female"
                    } else {
                        gender = "Other"
                    }
                    binding.genderTxt.text = "Gender : " + gender
                    binding.bloodGroupTxt.text = "Blood Group : " + state.data.response.blood_group
                    val nationality = state.data.response.nationality
                    val formattedNationality = if (!nationality.isNullOrEmpty()) {
                        nationality.replaceFirstChar { it.uppercase() }
                    } else {
                        "Not Given"
                    }
                    binding.nationalityTxt.text = "Nationality : " + formattedNationality
                    binding.imgProfile.borderColor =
                        ContextCompat.getColor(requireContext(), R.color.blue1)
                    binding.imgProfile.borderWidth = 4
                    if (state.data.response.image.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(Constants.IMAGES_URL + state.data.response.image).error(
                            ContextCompat.getDrawable(requireContext(), R.drawable.message_profile)
                        ).into(binding.imgProfile)
                    } else {
                        binding.imgProfile.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.message_profile
                            )
                        )
                    }
                }

                is UiState.Error -> {
                    binding.mainLo.visibility = View.GONE
                    binding.progress.hideProgress()

                }
            }
        }
    }

    private fun fetchProfileDetails() {
        val request = ProfileApiRequest(
            userDetails[User.SCHOOL_ID]!!,
            userDetails[User.ID]!!
        )
        viewModel.fetchProfileDetails(request)
        Log.d("requestLoading", request.toString())
    }

    private fun initViewModel() {
        val repository = ProfileRepository(requireContext())
        viewModel = ViewModelProvider(this, ViewModelFactory {
            ProfileViewModel(repository)
        })[ProfileViewModel::class.java]
    }

}