package com.iprism.school.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.parentapp.model.notifications.NotificationsItem
import com.iprism.school.model.notifications.NotificationsRequest
import com.iprism.school.adapters.NotificationsAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityNotificationsBinding
import com.iprism.school.repositories.ProfileRepository
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.getUserDetails
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.NotificationsViewModel
import com.iprism.school.viewModels.ViewModelFactory

class NotificationsActivity : BaseActivity() {

    private lateinit var binding : ActivityNotificationsBinding
    private lateinit var viewModel: NotificationsViewModel
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var notifications = mutableListOf<NotificationsItem>()
    private lateinit var hospitalsAdapter: NotificationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        setUpNotifications()
        initViewModel()
        observeResponse()
        fetchNotifications()
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }


    private fun setUpNotifications() {
        hospitalsAdapter = NotificationsAdapter(notifications as ArrayList<NotificationsItem?>)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.notificationsRv.apply {
            layoutManager = linearLayoutManager
            adapter = hospitalsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreDoctors()
                        }
                    }
                }
            })
        }
    }

    private fun initViewModel() {
        val repository = ProfileRepository(this)
        val factory = ViewModelFactory { NotificationsViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[NotificationsViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false
                    hospitalsAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.videos
                    if (newBookings.isNotEmpty()) {
                        notifications.addAll(newBookings)
                        hospitalsAdapter.notifyDataSetChanged()
                        if (result.data.response.pagination.totalPages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    isLoading = false
                    hospitalsAdapter.removeLoadingFooter()
                    //requireContext().showToast(result.message)
                    if (result.message.equals("no data found", true)) {
                        binding.noDataLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun fetchNotifications() {
        val userDetails = getUserDetails()
        val addressListRequest = NotificationsRequest(userDetails[User.ID]!!,userDetails[User.STUDENT_ID].toString(), userDetails[User.SCHOOL_ID].toString(), currentPage)
        NetworkRetryHelper.checkAndCallWithRetry(this, addressListRequest) { req ->
            viewModel.fetchNotifications(req)
        }
        Log.d("requestLoading", addressListRequest.toString())
    }

    private fun loadMoreDoctors() {
        isLoading = true
        currentPage += 1
        hospitalsAdapter.showLoadingFooter()
        fetchNotifications()
    }
}