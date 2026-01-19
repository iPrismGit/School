package com.iprism.school.activities.album

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.school.R
import com.iprism.school.activities.DayCarePlansActivity
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityCreateAlbumsBinding
import com.iprism.school.databinding.ActivityCreateDayCareAlbumsBinding
import com.iprism.school.databinding.ActivityDayCareAlbumsBinding
import com.iprism.school.model.daycare.Category
import com.iprism.school.model.daycare.DayCareApiRequest
import com.iprism.school.repositories.DayCareRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.DayCareViewModel
import com.iprism.school.viewModels.ViewModelFactory

class CreateDayCareAlbumsActivity : BaseActivity() {

    private lateinit var binding: ActivityCreateDayCareAlbumsBinding
    private lateinit var viewModel: DayCareViewModel
    private var planId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateDayCareAlbumsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViewModel()
        handleBack()
        observePlansResponse()
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
                    planId = plans[position].id.toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

}