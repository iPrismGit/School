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
import com.iprism.school.databinding.ActivityNapBinding
import com.iprism.school.model.daycare.Category
import com.iprism.school.model.daycare.DayCareApiRequest
import com.iprism.school.repositories.DayCareRepository
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.DayCareViewModel
import com.iprism.school.viewModels.DiaryViewModel
import com.iprism.school.viewModels.ViewModelFactory
import kotlin.math.tan

class NapActivity : BaseActivity() {

    private lateinit var binding: ActivityNapBinding
    private lateinit var viewModel: DayCareViewModel
    private var planId = ""
    private var studentId = ""
    private var type = ""
    private var id = ""
    private var napType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        planId = intent.getStringExtra("planId").toString()
        studentId = intent.getStringExtra("studentId").toString()
        type = intent.getStringExtra("type").toString()
        id = intent.getStringExtra("id").toString()
        binding.napRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.long_nap_rb -> {
                    napType = "Long Nap - 2 Hours"
                }

                R.id.short_nap_rb -> {
                    napType = "Short Nap - 20 Minutes"
                }

            }
        }
        handleBack()
        handleSubmitBtn()
        handleStartTimeLo()
        handleEndTimeLo()
        initViewModel()
        observeInsertDaycareReportResponse()
    }

    private fun initViewModel() {
        val repository = DayCareRepository(this)
        val factory = ViewModelFactory { DayCareViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[DayCareViewModel::class.java]
    }

    private fun handleEndTimeLo() {
        binding.wakeupTimeLo.setOnClickListener { view ->
            DateTimeUtils.getTime(binding.wakeupTimeTxt)
        }
    }

    private fun handleStartTimeLo() {
        binding.startTimeTxt.setOnClickListener { view ->
            DateTimeUtils.getTime(binding.startTimeTxt)
        }
    }

    private fun getStartTime() : String{
        return binding.startTimeTxt.text.toString().trim()
    }

    private fun getWakeupTime() : String{
        return binding.wakeupTimeTxt.text.toString().trim()
    }

    private fun getMessageTime() : String{
        return binding.messageTxt.text.toString().trim()
    }

    private fun handleSubmitBtn() {
        binding.submitBtn.setOnClickListener { view ->
            if (getStartTime().isEmpty()){
                ToastUtils.showErrorCustomToast(this, "Please Select Nap Starting Time..!")
            } else if (getWakeupTime().isEmpty()){
                ToastUtils.showErrorCustomToast(this, "Please Select Nap Wakeup Time..!")
            } else if(napType.isEmpty()){
                ToastUtils.showErrorCustomToast(this, "Please Select Nap Type..!")
            } else{
               var request = DayCareApiRequest(userDetails[User.ACADEMIC_YEAR_ID].toString(),
                   napType, "", userDetails[User.SCHOOL_ID].toString(), id, "",
                   getMessageTime(), 1, studentId, getStartTime(),
                   userDetails[User.ID].toString(), "insert", getWakeupTime())
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
                    intent.putExtra("tag", "Nap Added ")
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