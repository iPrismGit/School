package com.iprism.school.activities.circular

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.iprism.school.activities.HomeActivity
import com.iprism.school.activities.ViewImageActivity
import com.iprism.school.adapters.CalenderAdapter
import com.iprism.school.adapters.CircularsAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityConsentsBinding
import com.iprism.school.interfaces.OnCalenderClickListener
import com.iprism.school.model.circularmodels.Circular
import com.iprism.school.model.circularmodels.CircularApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.eventsmodel.Event
import com.iprism.school.model.eventsmodel.EventsApiRequest
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.CircularRepository
import com.iprism.school.repositories.EventsRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.CircularViewModel
import com.iprism.school.viewModels.ViewModelFactory

class ConsentsActivity : BaseActivity() {

    private lateinit var binding: ActivityConsentsBinding
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var circularViewModel: CircularViewModel
    private var academicYear: String = ""
    private var academicYearId: String = ""
    private lateinit var circularsAdapter: CircularsAdapter
    private var circularList = mutableListOf<Circular>()
    private var isFreshLoad = false
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        initViewModel()
        observeAcademicYearsResponse()
        setupRecyclerView()
        observeCircularsResponse()
        handleRefreshLo()
        var request = ClassTeacherApiRequest("", userDetails[User.ID].toString(), "academic_year")
        attendanceViewModel.fetchAcademicYears(request)
    }

    private fun loadEvents(isFromFilterChange: Boolean = false) {

        if (isLoading) return

        if (isFromFilterChange) {
            currentPage = 1
            isLastPage = false
            isFreshLoad = true

            circularList.clear()
            circularsAdapter.notifyDataSetChanged()

            binding.circularsRv.visibility = View.GONE
            binding.noDataFoundTxt.visibility = View.VISIBLE
        }

        isLoading = true

        val request = CircularApiRequest(
            academicYearId,
            userDetails[User.SCHOOL_ID].toString(),
            currentPage,
            userDetails[User.ID].toString())

        Log.d("CircularApiRequest", request.toString())
        circularViewModel.fetchCirculars(request)
    }

    private fun refreshItems() {
        currentPage = 1
        isLastPage = false
        isLoading = false
        circularList.clear()
        circularsAdapter.notifyDataSetChanged()
        loadEvents(isFromFilterChange = true)
    }

    private fun loadMoreItems() {
        if (isLastPage || isLoading) return
        isLoading = true
        currentPage++
        loadEvents()
    }

    private fun resetEvents() {
        currentPage = 1
        isLastPage = false
        isLoading = false

        circularList.clear()
        circularsAdapter.notifyDataSetChanged()

        binding.circularsRv.visibility = View.GONE
        binding.noDataFoundTxt.visibility = View.VISIBLE
    }

    private fun handleRefreshLo() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                refreshItems()
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun setupRecyclerView() {
        circularsAdapter = CircularsAdapter(this, circularList)
        val linearLayoutManager = LinearLayoutManager(this)

        binding.circularsRv.apply {
            layoutManager = linearLayoutManager
            adapter = circularsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled =
                        !binding.circularsRv.canScrollVertically(-1)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage && circularList.isNotEmpty()) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            loadMoreItems()
                        }
                    }

                }
            })

            circularsAdapter.setupListener(object : OnCalenderClickListener{
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

    }

    private fun observeCircularsResponse() {
        circularViewModel.circularsResponse.observe(this) { result ->

            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newCirculars = result.data.circulars

                    if (newCirculars.isNotEmpty()) {

                        if (isFreshLoad) {
                            circularList.clear()
                            isFreshLoad = false
                        }

                        circularList.addAll(newCirculars)
                        circularsAdapter.notifyDataSetChanged()

                        binding.circularsRv.visibility = View.VISIBLE
                        binding.noDataFoundTxt.visibility = View.GONE

                        isLastPage = newCirculars.size < limit
                    } else {
                        isLastPage = true

                        if (currentPage == 1) {
                            circularList.clear()
                            circularsAdapter.notifyDataSetChanged()
                            binding.circularsRv.visibility = View.GONE
                            binding.noDataFoundTxt.visibility = View.VISIBLE
                        }
                    }

                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    if (circularList.isEmpty()) {
                        binding.circularsRv.visibility = View.GONE
                        binding.noDataFoundTxt.visibility = View.VISIBLE
                        ToastUtils.showErrorCustomToast(this, result.message)
                    } else {
                        binding.circularsRv.visibility = View.VISIBLE
                        binding.noDataFoundTxt.visibility = View.GONE
                        ToastUtils.showErrorCustomToast(this, "There is no more data")
                    }
                }
            }
        }
    }

    private fun observeAcademicYearsResponse() {
        attendanceViewModel.academicYearsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    academicYear = result.data.name
                    academicYearId = result.data.id
                    val request = CircularApiRequest(
                        academicYearId,
                        userDetails[User.SCHOOL_ID].toString(),
                        currentPage,
                        userDetails[User.ID].toString())

                    Log.d("CircularApiRequest", request.toString())
                    circularViewModel.fetchCirculars(request)
                    Log.d("AcademicYear", academicYear + ", " + academicYearId)
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    Log.d("Message", result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ConsentsActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    private fun initViewModel() {
        val repository = AttendanceRepository(this)
        val factory = ViewModelFactory { AttendanceViewModel(repository) }
        attendanceViewModel = ViewModelProvider(this, factory)[AttendanceViewModel::class.java]

        val circularsRepository = CircularRepository(this)
        val circularFactory = ViewModelFactory { CircularViewModel(circularsRepository) }
        circularViewModel = ViewModelProvider(this, circularFactory)[CircularViewModel::class.java]
    }

}

