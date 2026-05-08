package com.iprism.school.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.school.adapters.DayCareStudentsAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityDayCarePlansBinding
import com.iprism.school.interfaces.OnDayCareClickListener
import com.iprism.school.model.daycare.Category
import com.iprism.school.model.daycare.DayCareApiRequest
import com.iprism.school.model.daycare.Student
import com.iprism.school.repositories.DayCareRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.DayCareViewModel
import com.iprism.school.viewModels.ViewModelFactory

class DayCarePlansActivity : BaseActivity() {

    private lateinit var binding: ActivityDayCarePlansBinding
    private lateinit var viewModel: DayCareViewModel
    private var planId: String = ""
    private lateinit var studentsAdapter: DayCareStudentsAdapter
    private var studentsList = mutableListOf<Student?>()
    private var isFreshLoad = false
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDayCarePlansBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViewModel()
        handleBack()
        setupRecyclerView()
        observePlansResponse()
        observeResponse()
        handleRefreshLo()
        val request = DayCareApiRequest(
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

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

    private fun initViewModel() {
        val repository = DayCareRepository(this)
        val factory = ViewModelFactory { DayCareViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[DayCareViewModel::class.java]
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
                        val updatedList = result.data.categories.toMutableList()
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
        val namesList = plans.map { it.name }
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
                    if (!planId.equals("-1", true)) {
                        binding.studentsRv.visibility = View.VISIBLE
                        refreshItems()
                       // loadEvents()
                    } else{
                        studentsList.clear()
                        binding.studentsRv.visibility = View.GONE
                        binding.noDataTxt.visibility = View.VISIBLE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun setupRecyclerView() {
        studentsAdapter = DayCareStudentsAdapter(studentsList as ArrayList<Student?>)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.studentsRv.apply {
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
                            loadMoreItems()
                        }
                    }
                }
            })
        }
        studentsAdapter.setupListener(object : OnDayCareClickListener {
            override fun onItemLick(id: Int) {
                Log.d("DayCareIds", id.toString() + ", " + planId)
                val intent =
                    Intent(this@DayCarePlansActivity, DaycareActivitiesActivity::class.java)
                intent.putExtra("studentId", id.toString())
                intent.putExtra("planId", planId)
                startActivity(intent)
            }

        })

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.dayCareStudentsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.noDataTxt.visibility = View.GONE
                    binding.progress.hideProgress()
                    isLoading = false
                    studentsAdapter.removeLoadingFooter()
                    val newBookings = result.data.students
                    if (newBookings.isNotEmpty()) {
                        studentsList.addAll(newBookings)
                        studentsAdapter.notifyDataSetChanged()
                        if (result.data.pagination.total_pages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    studentsAdapter.removeLoadingFooter()
                    binding.progress.hideProgress()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataTxt.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun loadEvents() {

        val request = DayCareApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            "",
            "",
            userDetails[User.SCHOOL_ID].toString(),
            planId,
            "",
            "",
            currentPage,
            "",
            "",
            userDetails[User.ID].toString(),
            "students",
            ""
        )

        Log.d("StudentsApiRequest", request.toString())
        viewModel.fetchDayCareStudents(request)
    }

    private fun refreshItems() {
        currentPage = 1
        isLastPage = false
        isLoading = false
        studentsList.clear()
        studentsAdapter.notifyDataSetChanged()
        loadEvents()
    }

    private fun loadMoreItems() {
        if (isLastPage || isLoading) return
        isLoading = true
        currentPage++
        studentsAdapter.showLoadingFooter()
        loadEvents()
    }

    private fun handleRefreshLo() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                refreshItems()
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

}