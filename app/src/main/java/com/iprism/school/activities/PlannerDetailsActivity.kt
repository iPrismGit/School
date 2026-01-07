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
import com.iprism.school.R
import com.iprism.school.adapters.PlannerCategoriesAdapter
import com.iprism.school.adapters.PlannerDetailsAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityPlannerDetailsBinding
import com.iprism.school.interfaces.OnPlannerClickListener
import com.iprism.school.model.plannersandresources.Category
import com.iprism.school.model.plannersandresources.Pdf
import com.iprism.school.model.plannersandresources.PlannersAndResourcesApiRequest
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.repositories.PlannersRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AttendanceViewModel
import com.iprism.school.viewModels.PLannersAndResourcesViewModel
import com.iprism.school.viewModels.ViewModelFactory

class PlannerDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityPlannerDetailsBinding
    private lateinit var plannersViewModel: PLannersAndResourcesViewModel
    private var catId = ""
    private var category = ""
    private var id = ""
    private var subject = ""
    private var subCategory = ""
    private var description = ""
    private var academicYearId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlannerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        catId = intent.getStringExtra("catId").toString()
        category = intent.getStringExtra("category").toString()
        id = intent.getStringExtra("id").toString()
        subject = intent.getStringExtra("subject").toString()
        subCategory = intent.getStringExtra("subCategory").toString()
        description = intent.getStringExtra("description").toString()
        academicYearId = intent.getStringExtra("academicYearId").toString()
        setupData()
        handleBack()
        initViewModel()
        observePlannerDetailsResponse()
        var request = PlannersAndResourcesApiRequest(
            academicYearId,
            userDetails[User.SCHOOL_ID].toString(),
            catId,
            1,
            id,
            userDetails[User.ID].toString(),
            "planner_images"
        )
        plannersViewModel.fetchPlannerDetails(request)
        Log.d("PlannerDetailsRequest", request.toString())
    }

    private fun initViewModel() {
        val plannersRepository = PlannersRepository(this)
        val plannersFactory = ViewModelFactory { PLannersAndResourcesViewModel(plannersRepository) }
        plannersViewModel =
            ViewModelProvider(this, plannersFactory)[PLannersAndResourcesViewModel::class.java]
    }

    private fun observePlannerDetailsResponse() {
        plannersViewModel.plannerDetailsResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.noDataTxt.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.pdfs.isNotEmpty()) {
                        setupPlannerDetailsAdapter(result.data.pdfs)
                        binding.plannersRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                    } else {
                        binding.plannersRv.visibility = View.GONE
                        binding.noDataTxt.visibility = View.VISIBLE
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    binding.plannersRv.visibility = View.GONE
                    binding.noDataTxt.visibility = View.VISIBLE
                    ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
    }

    private fun setupPlannerDetailsAdapter(pdfs: List<Pdf>) {
        var plannerDetailsAdapter = PlannerDetailsAdapter(this, pdfs)
        binding.plannersRv.adapter = plannerDetailsAdapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.plannersRv.layoutManager = linearLayoutManager
        plannerDetailsAdapter.setupListener(object : OnPlannerClickListener {
            override fun onCategoryClick(id: String, catName: String) {

            }

            override fun onViewClick(pdfUrl: String) {
                var intent = Intent(this@PlannerDetailsActivity, PdfViewActivity::class.java)
                intent.putExtra("pdfUrl", pdfUrl)
                startActivity(intent)
            }

        })

    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

    private fun setupData() {
        binding.titleTxt.text = subject
        if (subCategory.isEmpty()) {
            binding.categoryTxt.text = category + " | N/A"
        } else {
            binding.categoryTxt.text = category + " | " + subCategory
        }
        if (description.isEmpty()) {
            binding.messageTxt.text = "N/A"
        } else {
            binding.messageTxt.text = description
        }

    }

}