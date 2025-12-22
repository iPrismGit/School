package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.adapters.StaffListAdapter
import com.iprism.school.databinding.ActivityStaffBinding
import com.iprism.school.model.Request.StaffListReq
import com.iprism.school.model.Request.StaffListResponse
import com.iprism.school.model.Request.StaffStatusReq
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.toString

class StaffActivity : BaseActivity() {

    private lateinit var binding : ActivityStaffBinding
    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var message_id: String = ""
    private var staffType: String = "active"
    private var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        tag = intent.getStringExtra("tag").toString()
        message_id = intent.getStringExtra("message_id").toString()


        binding.plusBtn.setOnClickListener {
            val intent = Intent(this@StaffActivity, CreateStaffActivity::class.java)
            intent.putExtra("tag","new")
            startActivity(intent)
        }

        binding.searchBtn.setOnClickListener {
            binding.searchrl.visibility = View.VISIBLE
            binding.searchBtn.visibility = View.GONE
        }

        binding.searchBtn1.setOnClickListener {
            if (binding.etSearch.text.toString() == "" || binding.etSearch.text.toString() == null) {
                showToast("Enter search key")
            } else {
                type = "search"
                staffList()
            }
        }

        binding.activeDetailsLl.setOnClickListener {
            staffType = "active"
            type = ""
            binding.etSearch.text.clear()
            binding.activeDetailsLl.setBackgroundResource(R.color.blue)
            binding.inactiveDetailsLl.setBackgroundResource(R.color.white)

            binding.activeDetailsLl.setTextColor(ContextCompat.getColor(this,R.color.white))
            binding.inactiveDetailsLl.setTextColor(ContextCompat.getColor(this,R.color.black))

            staffList()
        }

        binding.inactiveDetailsLl.setOnClickListener {
            staffType = "inactive"
            type = ""
            binding.etSearch.text.clear()
            binding.activeDetailsLl.setBackgroundResource(R.color.white)
            binding.inactiveDetailsLl.setBackgroundResource(R.color.blue)
            binding.activeDetailsLl.setTextColor(ContextCompat.getColor(this,R.color.black))
            binding.inactiveDetailsLl.setTextColor(ContextCompat.getColor(this,R.color.white))
            staffList()
        }

        staffList()

    }


    private fun staffList() {
        showProgress()
        var apiRequest = StaffListReq(auth_token,scl_id,binding.etSearch.text.toString(),staffType,teacherId,type)
        Log.d("staffList", apiRequest.toString())
        val call: Call<StaffListResponse> = parentApiService!!.staffList(apiRequest)
        call.enqueue(object : Callback<StaffListResponse> {
            override fun onResponse(call: Call<StaffListResponse>, response: Response<StaffListResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        binding.nodata.visibility = View.GONE
                        binding.rvList.visibility = View.VISIBLE

                        val adap1 = StaffListAdapter(this@StaffActivity, loginApiResponse.response.staff)
                        binding.rvList.layoutManager = LinearLayoutManager(this@StaffActivity, LinearLayoutManager.VERTICAL, false)
                        binding.rvList.adapter = adap1
                        adap1.notifyDataSetChanged()

                        adap1.deactiveCallBack = {
                            mydata ->
                            val staffId = mydata.id.toString()
                            var status = ""
                            if (mydata.status == "1"){
                                status = "deactivate"
                            }else{
                                status = "activate"
                            }
                            changesStatus(staffId,status)
                        }

                            adap1.OnItemCallEdit = {
                                mydata ->
                                val staffId = mydata.id.toString()
                                var status = ""
                                if (mydata.status == "1"){
                                    status = "deactivate"
                                }else{
                                    status = "activate"
                                }
                                val intent = Intent(this@StaffActivity, CreateStaffActivity::class.java)
                                intent.putExtra("staffId",staffId)
                                intent.putExtra("status",status)
                                intent.putExtra("tag","edit")
                                startActivity(intent)
                            }
                    }else{
                        binding.nodata.visibility = View.VISIBLE
                        binding.rvList.visibility = View.GONE

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@StaffActivity, response.message())
                }
            }
            override fun onFailure(call: Call<StaffListResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@StaffActivity, t.message.toString())
            }
        })
    }

    private fun changesStatus(staffId: String, status: String) {
        showProgress()
        var apiRequest = StaffStatusReq(auth_token,scl_id,staffId,teacherId,status)
        Log.d("statusChangeReq", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.staffStatus(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){
                        staffList()
                    }else{
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@StaffActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@StaffActivity, t.message.toString())
            }
        })
    }

}