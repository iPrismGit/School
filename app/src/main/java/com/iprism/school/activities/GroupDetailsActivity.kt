package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.adapters.GroupStudentsAdapter
import com.iprism.school.databinding.ActivityGroupDetailsBinding
import com.iprism.school.databinding.DeleteBottomSheetBinding
import com.iprism.school.model.Request.GroupDetailsReq
import com.iprism.school.model.Response.GroupDetailsResponse
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.Constants
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityGroupDetailsBinding
    private var isInfoVisible: Boolean = false

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var groupId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        groupId = intent.getStringExtra("groupId").toString()

        handleStudentsDropDown()
        handleBack()
        handleDeleteBtn()

        groupDetails()

        binding.deleteIv.setOnClickListener {
            groupDelete()
        }

    }

    private fun handleDeleteBtn() {
        binding.deleteIv.setOnClickListener(View.OnClickListener {
            showDeleteBottomSheet()
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun handleStudentsDropDown() {
        binding.arrowLo.setOnClickListener(View.OnClickListener {
            toggleInformationVisibility()
        })
    }

    private fun toggleInformationVisibility() {
        if (isInfoVisible) {
            binding.studentsLo.visibility = View.GONE
            binding.downArrow.setImageResource(R.drawable.down_arrow_img)
        } else {
            binding.studentsLo.visibility = View.VISIBLE
            binding.downArrow.setImageResource(R.drawable.up_arrow_img)
        }
        isInfoVisible = !isInfoVisible
    }

    private fun showDeleteBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = DeleteBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
        }

        binding.cancelBtn.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        binding.crossIv.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
        })

        binding.deleteButton.setOnClickListener(View.OnClickListener {
            bottomSheetDialog.dismiss()
            ToastUtils.showSuccessCustomToast(this, "Group Deleted Successfully")
            finish()
        })

        bottomSheetDialog.show()
    }


    private fun groupDetails() {
        showProgress()
        var apiRequest = GroupDetailsReq(auth_token,groupId,scl_id,teacherId)
        Log.d("groupDetailsReq", apiRequest.toString())
        val call: Call<GroupDetailsResponse> = parentApiService!!.groupDetails(apiRequest)
        call.enqueue(object : Callback<GroupDetailsResponse> {
            override fun onResponse(call: Call<GroupDetailsResponse>, response: Response<GroupDetailsResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        Glide.with(this@GroupDetailsActivity)
                            .load(Constants.IMAGES_URL+loginApiResponse.response.groups.attachment.toString())
                            .into(binding.proPic)

                        binding.groupNameTv.text = loginApiResponse.response.groups.group_name.toString()
                        binding.adminsTv.text = loginApiResponse.response.groups.group_admins_names.toString()
                        binding.schoolMembersTv.text = loginApiResponse.response.groups.school_members.toString()
                        binding.groupStudentsTv.text = "Students : "+loginApiResponse.response.groups.student_names.toString()
                        binding.groupDescriptionTv.text = loginApiResponse.response.groups.group_description.toString()

                        val adap1 = GroupStudentsAdapter(this@GroupDetailsActivity, loginApiResponse.response.groups.students_details)
                        binding.groupStudentsRv.layoutManager = LinearLayoutManager(this@GroupDetailsActivity, LinearLayoutManager.VERTICAL, false)
                        binding.groupStudentsRv.adapter = adap1
                        adap1.notifyDataSetChanged()


                        binding.editIv.setOnClickListener {
                            val intent = Intent(this@GroupDetailsActivity, CreateGroupActivity::class.java)
                            intent.putExtra("groupId",groupId)
                            intent.putExtra("groupImg",loginApiResponse.response.groups.attachment.toString())
                            intent.putExtra("groupName",loginApiResponse.response.groups.group_name.toString())
                            intent.putExtra("groupDescription",loginApiResponse.response.groups.group_description.toString())
                            intent.putExtra("admins",loginApiResponse.response.groups.group_admins_names.toString())
                            intent.putExtra("adminsId",loginApiResponse.response.groups.group_admins.toString())
                            intent.putExtra("schoolmem",loginApiResponse.response.groups.school_members.toString())
                            intent.putExtra("schoolmemId",loginApiResponse.response.groups.group_staff.toString())
                            intent.putExtra("students",loginApiResponse.response.groups.student_names.toString())
                            intent.putExtra("studentsId",loginApiResponse.response.groups.group_students.toString())
                            intent.putExtra("tag","edit")
                            startActivity(intent)
                        }
                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@GroupDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<GroupDetailsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@GroupDetailsActivity, t.message.toString())
            }
        })
    }


    private fun groupDelete() {
        showProgress()
        var apiRequest = GroupDetailsReq(auth_token,groupId,scl_id,teacherId)
        Log.d("groupDetailsReq", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.groupDelete(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        val intent = Intent(this@GroupDetailsActivity, GroupsActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@GroupDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@GroupDetailsActivity, t.message.toString())
            }
        })
    }

}