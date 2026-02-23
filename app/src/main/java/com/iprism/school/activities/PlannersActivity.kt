package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.school.R
import com.iprism.school.adapters.CircularsAdapter
import com.iprism.school.adapters.PlannersAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityPlannersBinding
import com.iprism.school.interfaces.OnCalenderClickListener
import com.iprism.school.model.circularmodels.Circular
import com.iprism.school.model.circularmodels.CircularApiRequest
import com.iprism.school.model.plannersandresources.Planner
import com.iprism.school.model.plannersandresources.PlannersAndResourcesApiRequest
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.CircularRepository
import com.iprism.school.repositories.PlannersRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.CircularViewModel
import com.iprism.school.viewModels.PLannersAndResourcesViewModel
import com.iprism.school.viewModels.ViewModelFactory

class PlannersActivity : BaseActivity() {

    private lateinit var binding: ActivityPlannersBinding
    private var catId = ""
    private var catName = ""
    private lateinit var plannersViewModel: PLannersAndResourcesViewModel
    private lateinit var plannersAdapter: PlannersAdapter
    private var plannersList = mutableListOf<Planner>()
    private var isFreshLoad = false
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlannersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        catId = intent.getStringExtra("catId").toString()
        catName = intent.getStringExtra("catName").toString()
        binding.titleTxt.text = catName
        initViewModel()
        setupRecyclerView()
        loadPlanners()
        observePlannersResponse()
        handleRefreshLo()
        handleBack()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun initViewModel() {
        val plannersRepository = PlannersRepository(this)
        val plannersFactory = ViewModelFactory { PLannersAndResourcesViewModel(plannersRepository) }
        plannersViewModel =
            ViewModelProvider(this, plannersFactory)[PLannersAndResourcesViewModel::class.java]
    }

    private fun loadPlanners(isFromFilterChange: Boolean = false) {

        if (isLoading) return

        if (isFromFilterChange) {
            currentPage = 1
            isLastPage = false
            isFreshLoad = true

            plannersList.clear()
            plannersAdapter.notifyDataSetChanged()

            binding.plannersRv.visibility = View.GONE
            binding.noDataTxt.visibility = View.VISIBLE
        }

        isLoading = true

        val request = PlannersAndResourcesApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            catId,
            currentPage,
            "",
            userDetails[User.ID].toString(), "planners"
        )

        Log.d("PlannersApiRequest", request.toString())
        plannersViewModel.fetchPlanners(request)
    }

    private fun refreshItems() {
        currentPage = 1
        isLastPage = false
        isLoading = false
        resetEvents()
        plannersList.clear()
        plannersAdapter.notifyDataSetChanged()
        loadPlanners(isFromFilterChange = true)
    }

    private fun loadMoreItems() {
        if (isLastPage || isLoading) return
        isLoading = true
        currentPage++
        loadPlanners()
    }

    private fun resetEvents() {
        currentPage = 1
        isLastPage = false
        isLoading = false

        plannersList.clear()
        plannersAdapter.notifyDataSetChanged()

        binding.plannersRv.visibility = View.GONE
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

    private fun setupRecyclerView() {
        plannersAdapter = PlannersAdapter(this, plannersList)
        val linearLayoutManager = LinearLayoutManager(this)

        binding.plannersRv.apply {
            layoutManager = linearLayoutManager
            adapter = plannersAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled =
                        !binding.plannersRv.canScrollVertically(-1)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage && plannersList.isNotEmpty()) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            loadMoreItems()
                        }
                    }

                }
            })

            plannersAdapter.setupListener(object : PlannersAdapter.OnPlannerOuterClickListener {
                override fun onItemClick(
                    id: String,
                    catId: String,
                    subject: String,
                    description: String,
                    category: String,
                    subCategory: String
                ) {
                    var intent = Intent(this@PlannersActivity, PlannerDetailsActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("catId", catId)
                    intent.putExtra("subject", subject)
                    intent.putExtra("description", description)
                    intent.putExtra("category", category)
                    intent.putExtra("subCategory", subCategory)
                    startActivity(intent)
                }

            })

        }

    }

    private fun observePlannersResponse() {
        plannersViewModel.plannersResponse.observe(this) { result ->

            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newCirculars = result.data.planners

                    if (newCirculars.isNotEmpty()) {

                        if (isFreshLoad) {
                            plannersList.clear()
                            isFreshLoad = false
                        }

                        plannersList.addAll(newCirculars)
                        plannersAdapter.notifyDataSetChanged()

                        binding.plannersRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE

                        isLastPage = newCirculars.size < limit
                    } else {
                        isLastPage = true

                        if (currentPage == 1) {
                            plannersList.clear()
                            plannersAdapter.notifyDataSetChanged()
                            binding.plannersRv.visibility = View.GONE
                            binding.noDataTxt.visibility = View.VISIBLE
                        }
                    }

                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    if (plannersList.isEmpty()) {
                        binding.plannersRv.visibility = View.GONE
                        binding.noDataTxt.visibility = View.VISIBLE
                        ToastUtils.showErrorCustomToast(this, result.message)
                    } else {
                        binding.plannersRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                        ToastUtils.showErrorCustomToast(this, "There is no more data")
                    }
                }
            }
        }
    }

}