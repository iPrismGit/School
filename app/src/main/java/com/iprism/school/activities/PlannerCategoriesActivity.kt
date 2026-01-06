package com.iprism.school.activities

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
import com.iprism.school.adapters.CircularsAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityConsentsBinding
import com.iprism.school.databinding.ActivityPlannerCategoriesBinding
import com.iprism.school.interfaces.OnPlannerClickListener
import com.iprism.school.model.circularmodels.Circular
import com.iprism.school.model.circularmodels.CircularApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.plannersandresources.Category
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

class PlannerCategoriesActivity : BaseActivity() {

    private lateinit var binding: ActivityPlannerCategoriesBinding
    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var plannersViewModel: PLannersAndResourcesViewModel
    private var academicYear: String = ""
    private var academicYearId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlannerCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViewModel()
        handleBack()
        observeAcademicYearsResponse()
        observePlannerCategoriesResponse()
        var request = ClassTeacherApiRequest("", userDetails[User.ID].toString(), "academic_year")
        attendanceViewModel.fetchAcademicYears(request)
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun initViewModel() {
        val repository = AttendanceRepository(this)
        val factory = ViewModelFactory { AttendanceViewModel(repository) }
        attendanceViewModel = ViewModelProvider(this, factory)[AttendanceViewModel::class.java]

        val plannersRepository = PlannersRepository(this)
        val plannersFactory = ViewModelFactory { PLannersAndResourcesViewModel(plannersRepository) }
        plannersViewModel = ViewModelProvider(this, plannersFactory)[PLannersAndResourcesViewModel::class.java]
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
                    val request = PlannersAndResourcesApiRequest(
                        academicYearId,
                        userDetails[User.SCHOOL_ID].toString(),
                        "",
                        1,
                        "",
                        userDetails[User.ID].toString(),
                        "categories")

                    Log.d("PlannersRequest", request.toString())
                    plannersViewModel.fetchPlannerCategories(request)
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

    private fun observePlannerCategoriesResponse() {
        plannersViewModel.plannerCategoriesResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.noDataTxt.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.categories.isNotEmpty()){
                        setupPlannerCategoriesAdapter(result.data.categories)
                        binding.categoriesRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                    }else{
                        binding.categoriesRv.visibility = View.GONE
                        binding.noDataTxt.visibility = View.VISIBLE
                    }
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    binding.categoriesRv.visibility = View.GONE
                    binding.noDataTxt.visibility = View.VISIBLE
                    ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
    }

    private fun setupPlannerCategoriesAdapter(categories : List<Category>){
        var categoriesAdapter = PlannerCategoriesAdapter(this, categories)
        binding.categoriesRv.adapter = categoriesAdapter
        var linearLayoutManager = LinearLayoutManager(this)
        binding.categoriesRv.layoutManager = linearLayoutManager
        categoriesAdapter.setupListener(object  : OnPlannerClickListener{
            override fun onCategoryClick(id: String) {
                ToastUtils.showErrorCustomToast(this@PlannerCategoriesActivity, "ID: " + id)
            }

        })
    }

}