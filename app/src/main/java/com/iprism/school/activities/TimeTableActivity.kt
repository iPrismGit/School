package com.iprism.school.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.iprism.school.model.timetable.TimeTableRequest
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityTimeTableBinding
import com.iprism.school.repositories.TimeTableRepository
import com.iprism.school.utils.Constants
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.TimeTableViewModel
import com.iprism.school.viewModels.ViewModelFactory

class TimeTableActivity : BaseActivity() {

    private lateinit var binding: ActivityTimeTableBinding
    private lateinit var viewModel: TimeTableViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTimeTableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener( binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        initViewModel()
        fetchTimeTable()
        setupObservers()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

    private fun fetchTimeTable() {
        val request = TimeTableRequest(
            userDetails[User.ID]!!,
            userDetails[User.SCHOOL_ID]!!,
            userDetails[User.STUDENT_ID]!!,
            "1"
        )
        viewModel.fetchTimeTable(request)
        Log.d("requestLoading", request.toString())
    }

    private fun initViewModel() {
        val repository = TimeTableRepository(this)
        val factory = ViewModelFactory { TimeTableViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[TimeTableViewModel::class.java]
    }

    private fun setupObservers() {
        viewModel.response.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.noTimeTableTxt.visibility = View.GONE
                    binding.imageIv.visibility = View.VISIBLE
                    /*Glide.with(this)
                        .load(Constants.IMAGES_URL + state.data.response.timeTable[0].image)
                        .error(R.drawable.dummy_logo)
                        .listener(object : RequestListener<Drawable> {

                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.progress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.progress.visibility = View.GONE
                                return false
                            }
                        })
                        .into(binding.imageIv)*/
                    Glide.with(this).load(Constants.IMAGES_URL + state.data.response.timeTable[0].image).into(binding.imageIv)
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    if (state.message.equals("no data found", true)) {
                        binding.noTimeTableTxt.visibility = View.VISIBLE
                        binding.imageIv.visibility = View.GONE
                    }
                }
            }
        }
    }
}