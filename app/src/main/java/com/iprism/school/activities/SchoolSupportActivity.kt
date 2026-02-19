package com.iprism.school.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.school.R
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivitySchoolSupportBinding
import com.iprism.school.model.contentpagesmodel.SchoolSupportApiRequest
import com.iprism.school.repositories.ContentPagesRepository
import com.iprism.school.repositories.HelpTutorialsRepository
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.ContentPagesViewModel
import com.iprism.school.viewModels.HelpTutorialsViewModel
import com.iprism.school.viewModels.MessagesViewModel
import com.iprism.school.viewModels.ViewModelFactory

class SchoolSupportActivity : BaseActivity() {

    private lateinit var binding: ActivitySchoolSupportBinding
    private lateinit var viewModel: ContentPagesViewModel
    private var lat = 0.0
    private var lon = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySchoolSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        initViewModel()
        setupObservers()
        fetchSupportDetails()
        handleGetDirections()
    }

    private fun handleGetDirections() {
        binding.directionsBtn.setOnClickListener { view ->

        }
    }

    private fun fetchSupportDetails() {
        var request = SchoolSupportApiRequest(
            userDetails[User.SCHOOL_ID]!!,
            userDetails[User.ID]!!
        )
        viewModel.fetchSchoolSupportDetails(request)
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

    private fun initViewModel() {
        val repository = ContentPagesRepository(this)
        viewModel = ViewModelProvider(this, ViewModelFactory {
            ContentPagesViewModel(repository)
        })[ContentPagesViewModel::class.java]
    }

    private fun setupObservers() {
        viewModel.schoolSupportResponse.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.mainLo.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.noDataFoundTxt.visibility = View.GONE
                    binding.mainLo.visibility = View.VISIBLE
                    binding.progress.hideProgress()
                    binding.emailTxt.text = state.data.response.email
                    binding.mobileTxt.text = state.data.response.mobile
                    binding.alternativeMobileTxt.text = state.data.response.alternate_mobile
                    binding.addressTxt.text = state.data.response.address
                    lat = state.data.response.lat
                    lon = state.data.response.lon
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    if (state.message.equals("no data found", true)) {
                        binding.noDataFoundTxt.visibility = View.VISIBLE
                        binding.mainLo.visibility = View.GONE
                    }
                }
            }
        }
    }

}