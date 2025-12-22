package com.iprism.school.activities.subjects

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.base.BaseActivity
import com.iprism.school.activities.ClassSubjectsActivity
import com.iprism.school.activities.HomeActivity
import com.iprism.school.adapters.SubjectsAdapter
import com.iprism.school.databinding.ActivitySubjectsBinding
import com.iprism.school.databinding.AddSubjectBottomSheetBinding
import com.iprism.school.databinding.EditSubjectBottomSheetBinding
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Response.SubjectsListResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubjectsActivity : BaseActivity() {

    private lateinit var binding: ActivitySubjectsBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        handleAddBtn()
        handleMoreIv()

        binding.backIv.setOnClickListener {
            val  intent = Intent(this@SubjectsActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        callSubjetcsList()
    }

    private fun handleMoreIv() {
        binding.moreIv.setOnClickListener(View.OnClickListener {
            showPopupMenu(it)
        })
    }

    private fun handleAddBtn() {
        binding.addBtn.setOnClickListener(View.OnClickListener {
            /*showAddSubjectBottomSheet()*/
            val intent = Intent(this@SubjectsActivity, AddSubjectActivity::class.java)
            intent.putExtra("tag","add")
            startActivity(intent)

        })
    }

    private fun showAddSubjectBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val subjectBinding = AddSubjectBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(subjectBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(com.iprism.school.R.drawable.rounded_bottom_sheet_background)
            subjectBinding.submitBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "Subject Added Successfully")
            })

            subjectBinding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            subjectBinding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

    private fun showEditSubjectBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val editBinding = EditSubjectBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(editBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(com.iprism.school.R.drawable.rounded_bottom_sheet_background)
            editBinding.submitBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "Subject Edited Successfully")
            })

            editBinding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            editBinding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(com.iprism.school.R.menu.class_subjects_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                com.iprism.school.R.id.class_subjects_lo -> {
                    startActivity(Intent(this, ClassSubjectsActivity::class.java))
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }


    private fun callSubjetcsList() {
        showProgress()
        var apiRequest = SchoolStaffReq(auth_token, scl_id, teacherId)
        Log.d("subject_List", apiRequest.toString())
        val call: Call<SubjectsListResponse> = parentApiService!!.subjectsList(apiRequest)
        call.enqueue(object : Callback<SubjectsListResponse> {
            override fun onResponse(call: Call<SubjectsListResponse>, response: Response<SubjectsListResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        binding.noDataTv.visibility = View.GONE
                        binding.subjectsRv.visibility = View.VISIBLE

                        val adap1 = SubjectsAdapter(
                            this@SubjectsActivity,
                            loginApiResponse.response.subjects
                        )
                        binding.subjectsRv.layoutManager = LinearLayoutManager(
                            this@SubjectsActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        binding.subjectsRv.adapter = adap1
                        adap1.notifyDataSetChanged()

                        adap1.OnItemCallBack = {
                                mydata ->
                            val subId = mydata.id.toString()
                            val subName = mydata.subject_name.toString()
                            val subType = mydata.subject_type.toString()
                            val subDescription = mydata.description.toString()
                            val intent =
                                Intent(this@SubjectsActivity, AddSubjectActivity::class.java)
                            intent.putExtra("subId",subId)
                            intent.putExtra("subName",subName)
                            intent.putExtra("subType",subType)
                            intent.putExtra("subDescription",subDescription)
                            intent.putExtra("tag","edit")
                            startActivity(intent)
                        }
                    }else{
                        binding.noDataTv.visibility = View.VISIBLE
                        binding.subjectsRv.visibility = View.GONE
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@SubjectsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SubjectsListResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@SubjectsActivity, t.message.toString())
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val  intent = Intent(this@SubjectsActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}