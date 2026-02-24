package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.school.R
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityFoodBinding
import com.iprism.school.databinding.StudentItemBinding
import com.iprism.school.model.daycare.DayCareApiRequest
import com.iprism.school.repositories.DayCareRepository
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.DayCareViewModel
import com.iprism.school.viewModels.ViewModelFactory

class FoodActivity : BaseActivity() {

    private lateinit var binding: ActivityFoodBinding
    private lateinit var viewModel: DayCareViewModel
    private var planId = ""
    private var studentId = ""
    private var type = ""
    private var id = ""
    private var percentage = ""
    private var foodType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        planId = intent.getStringExtra("planId").toString()
        studentId = intent.getStringExtra("studentId").toString()
        type = intent.getStringExtra("type").toString()
        id = intent.getStringExtra("id").toString()
        binding.foodPercentageRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.percent0_rb -> {
                    percentage = "0 %"
                }

                R.id.percent25_rb -> {
                    percentage = "25 %"
                }

                R.id.percent50_rb -> {
                    percentage = "50 %"
                }

                R.id.percent75_rb -> {
                    percentage = "75 %"
                }

                R.id.percent100_rb -> {
                    percentage = "100 %"
                }

            }
        }

        binding.foodTypeRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.break_fast_rb -> {
                    foodType = "Break Fast"
                }

                R.id.lunch_rb -> {
                    foodType = "Lunch"
                }

                R.id.brunch_rb -> {
                    foodType = "Brunch"
                }

                R.id.snacks_rb -> {
                    foodType = "Snacks"
                }

            }
        }

        handleBack()
        handleSubmitBtn()
        handleStartTimeLo()
        initViewModel()
        observeInsertDaycareReportResponse()
    }

    private fun initViewModel() {
        val repository = DayCareRepository(this)
        val factory = ViewModelFactory { DayCareViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[DayCareViewModel::class.java]
    }

    private fun handleStartTimeLo() {
        binding.timeTxt.setOnClickListener { view ->
            DateTimeUtils.getTime(binding.timeTxt)
        }
    }

    private fun getStartTime(): String {
        return binding.timeTxt.text.toString().trim()
    }

    private fun getMessageTime(): String {
        return binding.messageTxt.text.toString().trim()
    }

    private fun handleSubmitBtn() {
        binding.submitBtn.setOnClickListener { view ->
            if (getStartTime().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Select Time..!")
            } else if (percentage.isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Select Percentage..!")
            }  else if (foodType.isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Select Food Type..!")
            }else {
                var request = DayCareApiRequest(
                    userDetails[User.ACADEMIC_YEAR_ID].toString(),
                    percentage, foodType, userDetails[User.SCHOOL_ID].toString(), id, "",
                    getMessageTime(), 1, studentId, getStartTime(),
                    userDetails[User.ID].toString(), "insert", ""
                )
                viewModel.insertDaycareReport(request)
            }

        }
    }

    private fun observeInsertDaycareReportResponse() {
        viewModel.insertDayCareReportResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.submitBtn.isEnabled = false
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    var intent = Intent(this, SuccessActivity::class.java)
                    intent.putExtra("tag", "Food Feeding Updated ")
                    startActivity(intent)
                    binding.submitBtn.isEnabled = true

                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    binding.progress.hideProgress()
                    binding.submitBtn.isEnabled = true
                }
            }
        }
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

}