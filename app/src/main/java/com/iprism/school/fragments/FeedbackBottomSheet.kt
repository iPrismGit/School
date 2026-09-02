package com.iprism.school.fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.iprism.parentapp.model.appreview.AppReviewRequest
import com.iprism.school.databinding.FragmentFeedbackBottomSheetBinding
import com.iprism.school.repositories.AppReviewRepository
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.utils.showToast
import com.iprism.school.viewModels.AppReviewViewModel
import com.iprism.school.viewModels.ViewModelFactory
import com.smarteist.autoimageslider.IndicatorView.utils.DensityUtils.dpToPx

class FeedbackBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentFeedbackBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AppReviewViewModel
    private val issues = listOf(
        "App is slow",
        "App crashes",
        "Login issues",
        "Notifications not working",
        "Features are difficult to use",
        "UI/Design needs improvement",
        "Some features are missing",
        "Data is not updated"
    )

    private val selectedIssuesList = mutableSetOf<String>()

    private var selectedIssues = ""

    // Colors
    private val selectedColor = Color.parseColor("#24539A")
    private val unselectedColor = Color.WHITE

    private val selectedTextColor = Color.WHITE
    private val unselectedTextColor = Color.parseColor("#34313D")

    private val selectedStrokeColor = Color.parseColor("#24539A")
    private val unselectedStrokeColor = Color.parseColor("#D9DDE5")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedbackBottomSheetBinding.inflate(
            inflater,
            container,
            false
        )
        binding.crossImg.bringToFront()
        setupSubmitButton()
        initViewModel()
        observeResponse()
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog as? BottomSheetDialog
        val bottomSheet =
            dialog?.findViewById<View>(
                R.id.design_bottom_sheet
            )

        bottomSheet?.let { sheet ->

            val behavior = BottomSheetBehavior.from(sheet)

            // Open expanded
            behavior.state = BottomSheetBehavior.STATE_EXPANDED

            // Allow full height
            behavior.skipCollapsed = true

            // Make it occupy available screen
            sheet.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            sheet.requestLayout()
        }
    }

    private fun handleCrossImg() {
        binding.crossImg.setOnClickListener {
            dismiss()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRating()
        handleCrossImg()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun setupRating() {
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            when {
                rating >= 4 -> {
                    // Positive feedback
                    binding.layoutPositive.visibility = View.VISIBLE
                    binding.layoutNegative.visibility = View.GONE

                    //binding.submitBtn.isEnabled = true

                    selectedIssues = ""


                    binding.messageTxt.text = "✎  Message (optional)"
                }

                rating in 1f..3f -> {

                    // Negative feedback
                    binding.layoutPositive.visibility = View.GONE
                    binding.layoutNegative.visibility = View.VISIBLE

                    //binding.submitBtn.isEnabled = false

                    // Clear old selections
                    selectedIssuesList.clear()
                    selectedIssues = ""

                    // Create chips
                    setupIssues()

                    binding.messageTxt.text = "✎  Others (optional)"
                }

                else -> {

                    // No rating
                    binding.layoutPositive.visibility = View.GONE
                    binding.layoutNegative.visibility = View.GONE

                    //binding.submitBtn.isEnabled = false
                }
            }
        }
    }

    private fun getRating(): Int {
        return binding.ratingBar.rating.toInt()
    }

    private fun getComment(): String {
        return binding.etPositiveComment.text.toString().trim()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewModel() {
        val repository = AppReviewRepository(requireContext())
        val factory = ViewModelFactory { AppReviewViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[AppReviewViewModel::class.java]
    }

    private fun insertReview() {
        val user = User(requireContext())
        val userDetails = user.getNewUserDetails()
        val request = AppReviewRequest(
            selectedIssues,
            userDetails[User.ID]!!,
            userDetails[User.SCHOOL_ID]!!,
            userDetails[User.STUDENT_ID]!!,
            getComment(),
            "Android",
            getRating()
        )
        NetworkRetryHelper.checkAndCallWithRetry(requireContext(), request) { req ->
            viewModel.insertReview(req)
        }
        Log.d("requestLoading", request.toString())
    }

    private fun observeResponse() {
        viewModel.response.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    requireContext().showToast("Feedback Sent")
                    dismiss()
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    requireContext().showToast(result.message)
                }
            }
        }
    }

    private fun setupSubmitButton() {
        binding.submitBtn.setOnClickListener {
            if (getRating() == 0) {
                requireContext().showToast("Please select a rating")
            } else {
                insertReview()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupIssues() {

        binding.issuesChipGroup.removeAllViews()

        issues.forEach { issue ->

            val chip = Chip(requireContext())

            chip.text = issue

            // Multiple selection
            chip.isCheckable = true
            chip.isClickable = true

            // Text
            chip.setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                12f
            )

            // Font
            //chip.typeface = resources.getFont(R.font.inter_medium)

            // Chip height
            chip.minHeight = dpToPx(40)

            // Padding
            chip.chipStartPadding = dpToPx(12).toFloat()
            chip.chipEndPadding = dpToPx(12).toFloat()

            // Rounded corners
            chip.chipCornerRadius = dpToPx(20).toFloat()

            // Border
            chip.chipStrokeWidth = dpToPx(1).toFloat()

            // Default style
            setUnselectedChipStyle(chip)

            chip.setOnCheckedChangeListener { button, isChecked ->

                if (isChecked) {

                    selectedIssuesList.add(issue)

                    setSelectedChipStyle(button as Chip)

                } else {

                    selectedIssuesList.remove(issue)

                    setUnselectedChipStyle(button as Chip)
                }

                selectedIssues =
                    selectedIssuesList.joinToString(", ")

                Log.d(
                    "SelectedIssues",
                    selectedIssues
                )
            }

            binding.issuesChipGroup.addView(chip)
        }
    }


    /**
     * Selected chip design
     */
    private fun setSelectedChipStyle(
        chip: Chip
    ) {

        chip.chipBackgroundColor =
            ColorStateList.valueOf(
                selectedColor
            )

        chip.chipStrokeColor =
            ColorStateList.valueOf(
                selectedStrokeColor
            )

        chip.setTextColor(
            selectedTextColor
        )

        // Selected icon
        chip.isCheckedIconVisible = true

        chip.setCheckedIconResource(
            com.iprism.school.R.drawable.selected_img
        )
    }


    /**
     * Unselected chip design
     */
    private fun setUnselectedChipStyle(
        chip: Chip
    ) {

        chip.chipBackgroundColor =
            ColorStateList.valueOf(
                unselectedColor
            )

        chip.chipStrokeColor =
            ColorStateList.valueOf(
                unselectedStrokeColor
            )

        chip.setTextColor(
            unselectedTextColor
        )

        chip.isCheckedIconVisible = false
    }
}