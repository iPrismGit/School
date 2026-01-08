package com.iprism.school.activities.calender

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.school.activities.ViewImageActivity
import com.iprism.school.base.BaseActivity

import com.iprism.school.adapters.CalenderAdapter
import com.iprism.school.databinding.ActivityCalenderBinding
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

@RequiresApi(Build.VERSION_CODES.O)
class CalenderActivity : BaseActivity() {

    private lateinit var binding: ActivityCalenderBinding
    private var currentCalendar = Calendar.getInstance()
    private var selectedCalendar = Calendar.getInstance()
    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var eventsAdapter: CalenderAdapter
    private var eventsList = mutableListOf<Event>()
    private var isFreshLoad = false
    private var selectedMonth = 0
    private var selectedYear = 0
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10
    private var classId: String = "-1"
    private var sectionId: String = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleRightArrow()
        handleLeftArrow()
        handleBack()
        updateDateText()
        initViewModel()
        setupRecyclerView()
        observeEventsResponse()
        handleRefreshLo()
        observeClassesResponse()
        observeSectionsResponse()
        var requestClasses = ClassTeacherApiRequest("", userDetails[User.ID].toString(),userDetails[User.SCHOOL_ID].toString(),userDetails[User.ACADEMIC_YEAR_ID].toString(), "classes")
        attendanceViewModel.fetchClasses(requestClasses)
    }

    private fun initViewModel() {
        val repository = AttendanceRepository(this)
        val factory = ViewModelFactory { AttendanceViewModel(repository) }
        attendanceViewModel = ViewModelProvider(this, factory)[AttendanceViewModel::class.java]

        val eventsRepository = EventsRepository(this)
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
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            classId,
            selectedMonth,
            currentPage,
            sectionId,
            userDetails[User.ID].toString(),
            selectedYear
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
        eventsAdapter = CalenderAdapter(this, eventsList)
        val linearLayoutManager = LinearLayoutManager(this)

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

            eventsAdapter.setupListener(object : OnCalenderClickListener{
                override fun onItemClick(
                    calenderId: String,
                    calenderName: String,
                    image: String
                ) {
                    if (image != null && image.isNotEmpty()){
                        val intent = Intent(this@CalenderActivity, ViewImageActivity::class.java)
                        intent.putExtra("EventImage", image)
                        intent.putExtra("EventName", calenderName)
                        startActivity(intent)
                    }else{
                        ToastUtils.showErrorCustomToast(this@CalenderActivity, "No Image Found..!")
                    }
                }

            })

        }

    }

    private fun observeEventsResponse() {
        eventsViewModel.eventsResponse.observe(this) { result ->

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
                        ToastUtils.showErrorCustomToast(this, result.message)
                    } else {
                        binding.calendersRv.visibility = View.VISIBLE
                        binding.noDataFoundLo.visibility = View.GONE
                        ToastUtils.showErrorCustomToast(this, "There is no more data")
                    }
                }
            }
        }
    }

    private fun observeClassesResponse() {
        attendanceViewModel.classesResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.classes.isNotEmpty()) {
                        var updatedList = result.data.classes.toMutableList()
                        updatedList.add(0, Class("-1", "Select Class"))
                        setupClassesAdapter(updatedList)
                    } else {
                        ToastUtils.showErrorCustomToast(this, "No Classes Found..!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun observeSectionsResponse() {
        attendanceViewModel.sectionsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.sections.isNotEmpty()) {
                        var updatedList = result.data.sections.toMutableList()
                        updatedList.add(0, Section("-1", "Select Section"))
                        setupSectionsAdapter(updatedList)
                    } else {
                        ToastUtils.showErrorCustomToast(this, "No Classes Found..!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun setupClassesAdapter(genderTypes: List<Class>) {
        var namesList = genderTypes.map { it.class_name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.classesSp.adapter = adapter
        binding.classesSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    classId = genderTypes[position].class_id.toString()
                    if (!classId.equals("-1", true)) {
                        var requestClasses = ClassTeacherApiRequest(
                            classId,
                            userDetails[User.ID].toString(),
                            userDetails[User.SCHOOL_ID].toString(),
                            userDetails[User.ACADEMIC_YEAR_ID].toString(),
                            "sections"
                        )
                        attendanceViewModel.fetchSections(requestClasses)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun setupSectionsAdapter(genderTypes: List<Section>) {
        var namesList = genderTypes.map { it.section_name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sectionsSp.adapter = adapter
        binding.sectionsSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sectionId = genderTypes[position].section_id.toString()
                    resetEvents()
                    if (!sectionId.equals("-1", true)) {
                        if (selectedYear != 0 && selectedMonth != 0) {
                            loadEvents()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
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

    private fun updateDateText() {
        val dateFormat = SimpleDateFormat("MMM, yyyy", Locale.getDefault())
        binding.dateTxt.text = dateFormat.format(selectedCalendar.time)

        val isCurrentMonth =
            selectedCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                    selectedCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)

     //   binding.rightArrowIv.visibility = if (isCurrentMonth) View.GONE else View.VISIBLE
        selectedMonth = selectedCalendar.get(Calendar.MONTH) + 1
        selectedYear = selectedCalendar.get(Calendar.YEAR)
        if (!classId.equals("-1", true) && !sectionId.equals("-1", true)) {
            refreshItems()
        }
    }

}
