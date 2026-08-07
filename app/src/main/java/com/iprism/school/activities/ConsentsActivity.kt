package com.iprism.school.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.school.adapters.CircularsAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityConsentsBinding
import com.iprism.school.interfaces.OnCalenderClickListener
import com.iprism.school.model.circularmodels.Circular
import com.iprism.school.model.circularmodels.CircularApiRequest
import com.iprism.school.repositories.CircularRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.CircularViewModel
import com.iprism.school.viewModels.ViewModelFactory

class ConsentsActivity : BaseActivity() {

    private lateinit var binding: ActivityConsentsBinding
    private lateinit var viewModel: CircularViewModel
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var circularsItems = mutableListOf<Circular>()
    private lateinit var circularsAdapter: CircularsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        handleBack()
        setUpCirculars()
        initViewModel()
        observeResponse()
        fetchCirculars()
        refresh()
    }

    private fun refresh() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                currentPage = 1
                isLastPage = false
                isLoading = false
                circularsItems.clear()
                circularsAdapter.notifyDataSetChanged()
                fetchCirculars()
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun setUpCirculars() {
        circularsAdapter = CircularsAdapter(circularsItems as ArrayList<Circular?>)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.circularsRv.apply {
            layoutManager = linearLayoutManager
            adapter = circularsAdapter
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
        circularsAdapter.setupListener(object : OnCalenderClickListener {
            override fun onItemClick(
                calenderId: String,
                calenderName: String,
                image: String
            ) {
                if (image != null && image.isNotEmpty()){
                    val intent = Intent(this@ConsentsActivity, ViewImageActivity::class.java)
                    intent.putExtra("EventImage", image)
                    intent.putExtra("EventName", calenderName)
                    startActivity(intent)
                }else{
                    ToastUtils.showErrorCustomToast(this@ConsentsActivity, "No Image Found..!")
                }
            }
        })
    }


    private fun initViewModel() {
        val repository = CircularRepository(this)
        val factory = ViewModelFactory { CircularViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[CircularViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.circularsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.noDataFoundTxt.visibility = View.GONE
                    binding.progress.hideProgress()
                    isLoading = false
                    circularsAdapter.removeLoadingFooter()
                    val newBookings = result.data.circulars
                    if (newBookings.isNotEmpty()) {
                        circularsItems.addAll(newBookings)
                        circularsAdapter.notifyDataSetChanged()
                        if (result.data.pagination.total_pages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    circularsAdapter.removeLoadingFooter()
                    binding.progress.hideProgress()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataFoundTxt.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun fetchCirculars() {
        val request = CircularApiRequest(
            userDetails[User.Companion.ACADEMIC_YEAR_ID].toString(),
            userDetails[User.Companion.SCHOOL_ID].toString(),
            currentPage,
            userDetails[User.Companion.ID].toString()
        )
        viewModel.fetchCirculars(request)
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreDoctors() {
        isLoading = true
        currentPage += 1
        circularsAdapter.showLoadingFooter()
        fetchCirculars()
    }
}