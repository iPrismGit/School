package com.iprism.school.activities.calender

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.LoginActivity
import com.iprism.school.databinding.ActivityCalenderDetailsBinding
import com.iprism.school.databinding.DeleteBottomSheetBinding
import com.iprism.school.model.Request.CalenderDeleteReq
import com.iprism.school.model.Request.TeacherCalederDetailsReq
import com.iprism.school.utils.User
import com.iprism.school.viewModels.Scl_ViewModel

class CalenderDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityCalenderDetailsBinding

    private val viewModel: Scl_ViewModel by viewModels()
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var formattedDateString: String = ""
    private var isInfoVisible: Boolean = false

    private var calenderId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalenderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        calenderId = intent.getStringExtra("calenderId").toString()
        Log.d("calenderId", calenderId)

        handleBack()
        handleEdit()
        handleDelete()
        handleDownArrow()
        calenderDetails()
    }

    private fun handleDownArrow() {
        binding.downArrow.setOnClickListener(View.OnClickListener {
            toggleInformationVisibility()
        })
    }

    private fun toggleInformationVisibility() {
        if (isInfoVisible) {
            binding.detailsLo.visibility = View.GONE
            binding.downArrow.setImageResource(R.drawable.down_arrow_img)
        } else {
            binding.detailsLo.visibility = View.VISIBLE
            binding.downArrow.setImageResource(R.drawable.up_arrow_img)
        }
        isInfoVisible = !isInfoVisible
    }

    private fun handleDelete() {
        binding.deleteButton.setOnClickListener(View.OnClickListener {
            showDeleteBottomSheet()
        })
    }

    private fun handleEdit() {
        binding.editIv.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, EditCalenderActivity::class.java)
            intent.putExtra("calenderId", calenderId)
            intent.putExtra("tag", "edit")
            startActivity(intent)
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }


    private fun showDeleteBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val deleteBinding = DeleteBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(deleteBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }
        deleteBinding.cancelBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        deleteBinding.crossIv.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        deleteBinding.deleteButton.setOnClickListener(View.OnClickListener {
            callDelete(bottomSheetDialog)
//            bottomSheetDialog.dismiss()
//            ToastUtils.showSuccessCustomToast(this, "Event Deleted Successfully")
//            finish()
        })
        bottomSheetDialog.show()
    }

    private fun callDelete(bottomSheetDialog: BottomSheetDialog) {
        showProgress()
        var apiRequest = CalenderDeleteReq(auth_token,calenderId,scl_id,teacherId)
        Log.d("calenderDeleteReq", apiRequest.toString())
        viewModel.calenderDelete(apiRequest).observe(this@CalenderDetailsActivity, Observer { response ->
            if (response != null && response.status == true) {
                hideProgress()
                bottomSheetDialog.dismiss()
                val  intent = Intent(this@CalenderDetailsActivity,CalenderActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                hideProgress()
                bottomSheetDialog.dismiss()
                if (response!!.message.toString() == "Authentication Token Expired"){
                    user!!.storeUserDetails("","","","","",""
                        ,"","","",""
                        ,"","","","",""
                        ,"","","")
                    startActivity(Intent(this@CalenderDetailsActivity, LoginActivity::class.java))
                    finish()
                }else{

                }
            }
        })
    }

    private fun calenderDetails() {
        showProgress()
        var apiRequest = TeacherCalederDetailsReq(auth_token,calenderId,scl_id,teacherId)
        Log.d("calenderDetailsReq", apiRequest.toString())
        viewModel.teacherCalenderDetials(apiRequest).observe(this@CalenderDetailsActivity, Observer { response ->
            if (response != null && response.status == true) {
                hideProgress()
                Log.d("calenderListResponse", response.toString())

                val date = response.response.calender_details[0].calender_date.toString()
                val day = response.response.calender_details[0].day.toString()

                binding.titleTxt.text = response.response.calender_details[0].subject.toString()
                binding.classTv.text = response.response.calender_details[0].class_names.toString()
                binding.usersTv.text = response.response.calender_details[0].staff_names.toString()
                binding.dateTv.text =  date.toString()+day.toString()
                binding.timeTv.text = response.response.calender_details[0].time.toString()
                binding.toTv.text = response.response.calender_details[0].student_names.toString()

            } else {
                hideProgress()
                if (response!!.message.toString() == "Authentication Token Expired"){
                    user!!.storeUserDetails("","","","","",""
                        ,"","","",""
                        ,"","","","",""
                        ,"","","")
                    startActivity(Intent(this@CalenderDetailsActivity, LoginActivity::class.java))
                    finish()
                }else{

                }
            }
        })
    }

}