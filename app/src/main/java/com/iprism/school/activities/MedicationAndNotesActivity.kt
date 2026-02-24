package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.iprism.school.R
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityMedicationAndNotesBinding
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

class MedicationAndNotesActivity : BaseActivity() {

    private lateinit var binding: ActivityMedicationAndNotesBinding
    private lateinit var viewModel: DayCareViewModel
    private var planId = ""
    private var studentId = ""
    private var type = ""
    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMedicationAndNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        planId = intent.getStringExtra("planId").toString()
        studentId = intent.getStringExtra("studentId").toString()
        type = intent.getStringExtra("type").toString()
        id = intent.getStringExtra("id").toString()
        if (type.equals("Medication", true)) {
            binding.medicationLo.visibility = View.VISIBLE
            binding.noteLo.visibility = View.GONE
            binding.titleTxt.text = "Medication"
        } else {
            binding.medicationLo.visibility = View.GONE
            binding.noteLo.visibility = View.VISIBLE
            binding.titleTxt.text = "Notes"
        }
        handleBack()
        handleSubmitBtn()
        initViewModel()
        observeInsertDaycareReportResponse()
        handleTimeLo()
    }

    private fun handleTimeLo() {
        binding.startTimeLo.setOnClickListener { view ->
            DateTimeUtils.getTime(binding.startTimeTxt)
        }
    }

    private fun handleSubmitBtn() {
        binding.submitBtn.setOnClickListener { view ->
            if (type.equals("Medication", true)) {
                if (getTime().isEmpty()) {
                    ToastUtils.showErrorCustomToast(this, "Please Select Time..!")
                } else if (getMedicationNotes().isEmpty()) {
                    ToastUtils.showErrorCustomToast(this, "Please Enter Medication Notes..!")
                } else {
                    var request = DayCareApiRequest(
                        userDetails[User.ACADEMIC_YEAR_ID].toString(),
                        "", "", userDetails[User.SCHOOL_ID].toString(), id, "",
                        getMedicationNotes(), 1, studentId, getTime(),
                        userDetails[User.ID].toString(), "insert", ""
                    )
                    viewModel.insertDaycareReport(request)
                }
            } else {
                if (getNotes().isEmpty()) {
                    ToastUtils.showErrorCustomToast(this, "Please Enter Notes..!")
                } else {
                    var request = DayCareApiRequest(
                        userDetails[User.ACADEMIC_YEAR_ID].toString(),
                        "", "", userDetails[User.SCHOOL_ID].toString(), id, "",
                        getNotes(), 1, studentId, "",
                        userDetails[User.ID].toString(), "insert", ""
                    )
                    viewModel.insertDaycareReport(request)
                }
            }
        }
    }

    private fun initViewModel() {
        val repository = DayCareRepository(this)
        val factory = ViewModelFactory { DayCareViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[DayCareViewModel::class.java]
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
                    intent.putExtra("tag", "$type Added")
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

    private fun getTime(): String {
        return binding.startTimeTxt.text.toString().trim()
    }

    private fun getMedicationNotes(): String {
        return binding.medicationMessageTxt.text.toString().trim()
    }

    private fun getNotes(): String {
        return binding.noteTxt.text.toString().trim()
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

}