package com.iprism.school.activities

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
    private var planId : String = ""
    private lateinit var studentsAdapter: DayCareStudentsAdapter
    private var studentsList = mutableListOf<Student>()
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
        observeStudentsResponse()
        handleRefreshLo()
        var request = DayCareApiRequest(userDetails[User.ACADEMIC_YEAR_ID].toString(), "", "", userDetails[User.SCHOOL_ID].toString(), "", "", "", 1, "", "", userDetails[User.ID].toString(), "categories", "")
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
                    resetEvents()
                    if (!planId.equals("-1", true)){
                        loadEvents()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun setupRecyclerView() {
        studentsAdapter = DayCareStudentsAdapter(this, studentsList)
        val linearLayoutManager = LinearLayoutManager(this)

        binding.studentsRv.apply {
            layoutManager = linearLayoutManager
            adapter = studentsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled =
                        !binding.studentsRv.canScrollVertically(-1)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage && studentsList.isNotEmpty()) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            loadMoreItems()
                        }
                    }

                }
            })
            studentsAdapter.setupListener(object : OnDayCareClickListener{
                override fun onItemLick(id: Int) {
                    var intent = Intent(this@DayCarePlansActivity, DaycareActivitiesActivity::class.java)
                    intent.putExtra("studentId", id)
                    intent.putExtra("planId", planId)
                    startActivity(intent)
                }

            })
        }

    }

    private fun observeStudentsResponse() {
        viewModel.dayCareStudentsResponse.observe(this) { result ->

            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newCirculars = result.data.students

                    if (newCirculars.isNotEmpty()) {

                        if (isFreshLoad) {
                            studentsList.clear()
                            isFreshLoad = false
                        }

                        studentsList.addAll(newCirculars)
                        studentsAdapter.notifyDataSetChanged()

                        binding.studentsRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE

                        isLastPage = newCirculars.size < limit
                    } else {
                        isLastPage = true

                        if (currentPage == 1) {
                            studentsList.clear()
                            studentsAdapter.notifyDataSetChanged()
                            binding.studentsRv.visibility = View.GONE
                            binding.noDataTxt.visibility = View.VISIBLE
                        }
                    }

                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    if (studentsList.isEmpty()) {
                        binding.studentsRv.visibility = View.GONE
                        binding.noDataTxt.visibility = View.VISIBLE
                        ToastUtils.showErrorCustomToast(this, result.message)
                    } else {
                        binding.studentsRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                        ToastUtils.showErrorCustomToast(this, "There is no more data")
                    }
                }
            }
        }
    }

    private fun loadEvents(isFromFilterChange: Boolean = false) {

        if (isLoading) return

        if (isFromFilterChange) {
            currentPage = 1
            isLastPage = false
            isFreshLoad = true

            studentsList.clear()
            studentsAdapter.notifyDataSetChanged()

            binding.studentsRv.visibility = View.GONE
            binding.noDataTxt.visibility = View.VISIBLE
        }

        isLoading = true

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
            "")

        Log.d("StudentsApiRequest", request.toString())
        viewModel.fetchDayCareStudents(request)
    }

    private fun refreshItems() {
        currentPage = 1
        isLastPage = false
        isLoading = false
        studentsList.clear()
        studentsAdapter.notifyDataSetChanged()
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

        studentsList.clear()
        studentsAdapter.notifyDataSetChanged()

        binding.studentsRv.visibility = View.GONE
        binding.noDataTxt.visibility = View.VISIBLE
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