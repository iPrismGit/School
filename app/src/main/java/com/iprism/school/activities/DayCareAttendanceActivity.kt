package com.iprism.school.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.school.R
import com.iprism.school.adapters.DayCareStudentsAttendanceAdapter
import com.iprism.school.adapters.HelpTutorialAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityDayCareAttendanceBinding
import com.iprism.school.model.DayCareAttendanceApiRequest
import com.iprism.school.model.SelectedStudent
import com.iprism.school.model.daycare.Category
import com.iprism.school.model.daycare.DayCareApiRequest
import com.iprism.school.model.daycare.Student
import com.iprism.school.model.helptutorials.HelpTutorial
import com.iprism.school.model.helptutorials.HelpTutorialsApiRequest
import com.iprism.school.repositories.DayCareAttendanceRepository
import com.iprism.school.repositories.DayCareRepository
import com.iprism.school.repositories.HelpTutorialsRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.DayCareAttendanceViewModel
import com.iprism.school.viewModels.DayCareViewModel
import com.iprism.school.viewModels.HelpTutorialsViewModel
import com.iprism.school.viewModels.ViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DayCareAttendanceActivity : BaseActivity() {

    private lateinit var binding: ActivityDayCareAttendanceBinding
    private lateinit var viewModel: DayCareViewModel
    private lateinit var attendanceViewModel: DayCareAttendanceViewModel
    private var planId: String = ""
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var students = mutableListOf<Student>()
    private var selectedStudents = mutableListOf<SelectedStudent>()
    private lateinit var studentsAdapter: DayCareStudentsAttendanceAdapter
    private var currentDate: String = ""
    private var backendDate: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDayCareAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")
        currentDate = LocalDate.now().format(formatter)

        val formatterBackend = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        backendDate = LocalDate.now().format(formatterBackend)
        binding.dateTxt.text = currentDate
        handleBack()
        initViewModel()
        observePlansResponse()
        setUpAdapter()
        setupObservers()
        handleRefresh()
        var request = DayCareApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            "",
            "",
            userDetails[User.SCHOOL_ID].toString(),
            "",
            "",
            "",
            1,
            "",
            "",
            userDetails[User.ID].toString(),
            "categories",
            ""
        )
        viewModel.fetchDayCarePlans(request)
    }

    private fun handleRefresh() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                resetStudentsData()
                fetchStudents()
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

    private fun initViewModel() {
        val repository = DayCareRepository(this)
        val factory = ViewModelFactory { DayCareViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[DayCareViewModel::class.java]

        val daycareStudentsRepository = DayCareAttendanceRepository(this)
        attendanceViewModel = ViewModelProvider(this, ViewModelFactory {
            DayCareAttendanceViewModel(daycareStudentsRepository)
        })[DayCareAttendanceViewModel::class.java]

    }

    private fun observePlansResponse() {
        viewModel.dayCarePlansResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.categories.isNotEmpty()) {
                        var updatedList = result.data.categories.toMutableList()
                        updatedList.add(0, Category("-1", "Select Plan"))
                        setupPlansAdapter(updatedList)
                    } else {
                        ToastUtils.showErrorCustomToast(this, "No Daycare Plans Found..!")
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun setupPlansAdapter(plans: List<Category>) {
        var namesList = plans.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, namesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.plansSp.adapter = adapter
        binding.plansSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    planId = plans[position].cat_id.toString()
                    resetStudentsData()
                    fetchStudents()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun resetStudentsData() {
        currentPage = 1
        isLastPage = false
        isLoading = false
        students.clear()
        studentsAdapter.notifyDataSetChanged()
        binding.studentAttendanceRv.visibility = View.GONE
        binding.noDataTxt.visibility = View.VISIBLE
    }

    private fun fetchStudents() {
        val request = DayCareAttendanceApiRequest(
            "",
            "",
            userDetails[User.SCHOOL_ID]!!,
            planId,
            backendDate,
            "",
            currentPage,
            "",
            selectedStudents,
            userDetails[User.ID]!!,
            "view"
        )
        attendanceViewModel.fetchDayCareStudents(request)
        Log.d("requestLoading", request.toString())
    }

    private fun setUpAdapter() {
        studentsAdapter = DayCareStudentsAttendanceAdapter(students as ArrayList<Student?>)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.studentAttendanceRv.apply {
            layoutManager = linearLayoutManager
            adapter = studentsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreTutorials()
                        }
                    }
                }
            })
        }
    }

    private fun setupObservers() {
        attendanceViewModel.response.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.noDataTxt.visibility = View.GONE
                    binding.studentAttendanceRv.visibility = View.VISIBLE
                    binding.progress.hideProgress()
                    isLoading = false
                    studentsAdapter.removeLoadingFooter()
                    val newBookings = state.data.response.students
                    Log.d("StudentsList", state.data.response.students.toString())
                    if (newBookings.isNotEmpty()) {
                        students.addAll(newBookings)
                        studentsAdapter.notifyDataSetChanged()
                        if (state.data.response.pagination.total_pages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    studentsAdapter.removeLoadingFooter()
                    binding.progress.hideProgress()
                    ToastUtils.showErrorCustomToast(this, state.message)
                    if (state.message.equals("no data found", true)) {
                        binding.noDataTxt.visibility = View.VISIBLE
                        binding.studentAttendanceRv.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun loadMoreTutorials() {
        isLoading = true
        currentPage += 1
        studentsAdapter.showLoadingFooter()
        fetchStudents()
    }

}