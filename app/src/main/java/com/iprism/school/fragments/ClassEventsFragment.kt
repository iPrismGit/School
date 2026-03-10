package com.iprism.school.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.school.R
import com.iprism.school.activities.ViewImageActivity
import com.iprism.school.adapters.CalenderAdapter
import com.iprism.school.base.BaseFragment
import com.iprism.school.databinding.FragmentClassEventsBinding
import com.iprism.school.interfaces.OnCalenderClickListener
import com.iprism.school.model.classteachermodel.Class
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.Section
import com.iprism.school.model.eventsmodel.Event
import com.iprism.school.model.eventsmodel.EventsApiRequest
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.EventsRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.EventsViewModel
import com.iprism.school.viewModels.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ClassEventsFragment : BaseFragment() {

    private lateinit var binding: FragmentClassEventsBinding

    private var currentCalendar = Calendar.getInstance()
    private var selectedCalendar = Calendar.getInstance()
    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var eventsAdapter: CalenderAdapter
    private var eventsList = mutableListOf<Event>()
    private var isFreshLoad = false
    private var selectedMonth = 0
    private var selectedYear = 0
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClassEventsBinding.inflate(inflater, container, false)
        initViewModel()
        setupRecyclerView()
        handleRightArrow()
        handleLeftArrow()
        updateDateText()
        observeEventsResponse()
        handleRefreshLo()
        loadEvents()
        return binding.root
    }

    private fun initViewModel() {
        val eventsRepository = EventsRepository(requireContext())
        val eventsFactory = ViewModelFactory { EventsViewModel(eventsRepository) }
        eventsViewModel = ViewModelProvider(this, eventsFactory)[EventsViewModel::class.java]
    }

    private fun loadEvents(isFromFilterChange: Boolean = false) {

        if (isLoading) return

        if (isFromFilterChange) {
            currentPage = 1
            isLastPage = false
            isFreshLoad = true

            eventsList.clear()
            eventsAdapter.notifyDataSetChanged()

            binding.calendersRv.visibility = View.GONE
            binding.noDataFoundLo.visibility = View.VISIBLE
        }

        isLoading = true

        val request = EventsApiRequest(
            userDetails[User.Companion.SCHOOL_ID].toString(),
            selectedMonth,
            currentPage,
            userDetails[User.Companion.ID].toString(),
            selectedYear,
            "school"
        )

        Log.d("EventsApiRequest", request.toString())
        eventsViewModel.fetchEvents(request)
    }

    private fun refreshItems() {
        currentPage = 1
        isLastPage = false
        isLoading = false
        eventsList.clear()
        eventsAdapter.notifyDataSetChanged()
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

        eventsList.clear()
        eventsAdapter.notifyDataSetChanged()

        binding.calendersRv.visibility = View.GONE
        binding.noDataFoundLo.visibility = View.VISIBLE
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
        eventsAdapter = CalenderAdapter(requireContext(), eventsList)
        val linearLayoutManager = LinearLayoutManager(requireContext())

        binding.calendersRv.apply {
            layoutManager = linearLayoutManager
            adapter = eventsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled =
                        !binding.calendersRv.canScrollVertically(-1)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage && eventsList.isNotEmpty()) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            loadMoreItems()
                        }
                    }

                }
            })

            eventsAdapter.setupListener(object : OnCalenderClickListener {
                override fun onItemClick(
                    calenderId: String,
                    calenderName: String,
                    image: String
                ) {
                    if (image != null && image.isNotEmpty()) {
                        val intent = Intent(requireContext(), ViewImageActivity::class.java)
                        intent.putExtra("EventImage", image)
                        intent.putExtra("EventName", calenderName)
                        startActivity(intent)
                    } else {
                        ToastUtils.showErrorCustomToast(requireContext(), "No Image Found..!")
                    }
                }

            })

        }

    }

    private fun observeEventsResponse() {
        eventsViewModel.eventsResponse.observe(viewLifecycleOwner) { result ->

            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newEvents = result.data.events

                    if (newEvents.isNotEmpty()) {

                        if (isFreshLoad) {
                            eventsList.clear()
                            isFreshLoad = false
                        }

                        eventsList.addAll(newEvents)
                        eventsAdapter.notifyDataSetChanged()

                        binding.calendersRv.visibility = View.VISIBLE
                        binding.noDataFoundLo.visibility = View.GONE

                        isLastPage = newEvents.size < limit
                    } else {
                        isLastPage = true

                        if (currentPage == 1) {
                            eventsList.clear()
                            eventsAdapter.notifyDataSetChanged()
                            binding.calendersRv.visibility = View.GONE
                            binding.noDataFoundLo.visibility = View.VISIBLE
                        }
                    }

                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    if (eventsList.isEmpty()) {
                        binding.calendersRv.visibility = View.GONE
                        binding.noDataFoundLo.visibility = View.VISIBLE
                        ToastUtils.showErrorCustomToast(requireContext(), result.message)
                    } else {
                        binding.calendersRv.visibility = View.VISIBLE
                        binding.noDataFoundLo.visibility = View.GONE
                        ToastUtils.showErrorCustomToast(requireContext(), "There is no more data")
                    }
                }
            }
        }
    }

    private fun handleRightArrow() {
        binding.rightArrowIv.setOnClickListener { view ->
            selectedCalendar.add(Calendar.MONTH, 1)
            selectedMonth = selectedCalendar.get(Calendar.MONTH) + 1
            selectedYear = selectedCalendar.get(Calendar.YEAR)
            updateDateText()
        }
    }

    private fun handleLeftArrow() {
        binding.leftArrowIv.setOnClickListener { view ->
            selectedCalendar.add(Calendar.MONTH, -1)
            selectedMonth = selectedCalendar.get(Calendar.MONTH) + 1
            selectedYear = selectedCalendar.get(Calendar.YEAR)
            updateDateText()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun updateDateText() {
        val dateFormat = SimpleDateFormat("MMM, yyyy", Locale.getDefault())
        binding.dateTxt.text = dateFormat.format(selectedCalendar.time)

        val isCurrentMonth =
            selectedCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                    selectedCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)

        //   binding.rightArrowIv.visibility = if (isCurrentMonth) View.GONE else View.VISIBLE
        selectedMonth = selectedCalendar.get(Calendar.MONTH) + 1
        selectedYear = selectedCalendar.get(Calendar.YEAR)
            refreshItems()
    }

}