package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.classes.CreateClassActivity
import com.iprism.school.adapters.ClassesAdapter
import com.iprism.school.databinding.ActivityClassesBinding
import com.iprism.school.databinding.FilterBottomSheetBinding
import com.iprism.school.model.Request.ClassListReq
import com.iprism.school.model.Response.ClassListResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassesActivity : BaseActivity() {

    private lateinit var binding: ActivityClassesBinding
    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var type: String = "active"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        handleMoreIv()
        handleFilterIv()
        callClassList()

        binding.activeDetailsLl.setOnClickListener {
            type = "active"
            binding.activeDetailsLl.setBackgroundResource(R.color.blue)
            binding.inactiveDetailsLl.setBackgroundResource(R.color.white)

            binding.activeDetailsLl.setTextColor(ContextCompat.getColor(this,R.color.white))
            binding.inactiveDetailsLl.setTextColor(ContextCompat.getColor(this,R.color.black))

            callClassList()
            binding.proTypeTv.text = "Active Classes"

        }

        binding.inactiveDetailsLl.setOnClickListener {
            type = "inactive"
            binding.activeDetailsLl.setBackgroundResource(R.color.white)
            binding.inactiveDetailsLl.setBackgroundResource(R.color.blue)
            binding.activeDetailsLl.setTextColor(ContextCompat.getColor(this,R.color.black))
            binding.inactiveDetailsLl.setTextColor(ContextCompat.getColor(this,R.color.white))
            callClassList()

            binding.proTypeTv.text = "Inactive Classes"
        }

        binding.plusBtn.setOnClickListener {
            val intent = Intent(this@ClassesActivity, CreateClassActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleFilterIv() {
        binding.filterBtn.setOnClickListener(View.OnClickListener {
            showFiltersBottomSheet()
        })
    }

    private fun handleMoreIv() {
        binding.more.setOnClickListener(View.OnClickListener {

        })
    }


    private fun showFiltersBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val filterBinding = FilterBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(filterBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            filterBinding.applyBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "Filters Applied..")
            })

            filterBinding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            filterBinding.removeFilterBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }

    private fun callClassList() {
        showProgress()
        var apiRequest = ClassListReq(auth_token,"","",scl_id,"",teacherId,type.toString())
        Log.d("class_ListReq", apiRequest.toString())
        val call: Call<ClassListResponse> = parentApiService!!.classList(apiRequest)
        call.enqueue(object : Callback<ClassListResponse> {
            override fun onResponse(call: Call<ClassListResponse>, response: Response<ClassListResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        binding.nodata.visibility = View.GONE
                        binding.rvList.visibility = View.VISIBLE

                        val adap1 = ClassesAdapter(this@ClassesActivity, loginApiResponse.response.classes)
                        binding.rvList.layoutManager = LinearLayoutManager(this@ClassesActivity, LinearLayoutManager.VERTICAL, false)
                        binding.rvList.adapter = adap1
                        adap1.notifyDataSetChanged()

                        adap1.OnItemCallBack = {
                                mydata ->
                            val classId = mydata.id.toString()
                            val class_name = mydata.class_name.toString()
                            val class_section = mydata.class_section.toString()
                            val class_session = mydata.class_session.toString()
                            val intent = Intent(this@ClassesActivity, CreateClassActivity::class.java)
                            intent.putExtra("classId",classId)
                            intent.putExtra("class_name",class_name)
                            intent.putExtra("class_section",class_section)
                            intent.putExtra("class_session",class_session)
                            intent.putExtra("tag","edit")
                            startActivity(intent)
                        }
                    }else{
                        binding.nodata.visibility = View.VISIBLE
                        binding.rvList.visibility = View.GONE
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@ClassesActivity, response.message())
                }
            }
            override fun onFailure(call: Call<ClassListResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@ClassesActivity, t.message.toString())
            }
        })
    }

}