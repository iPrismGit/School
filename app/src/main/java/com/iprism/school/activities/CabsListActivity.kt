package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.StaffActivity
import com.iprism.school.adapters.CabsListAdapter
import com.iprism.school.adapters.StaffListAdapter
import com.iprism.school.databinding.ActivityCabsListBinding
import com.iprism.school.databinding.ActivityFillOtherDetailsBinding
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Request.StaffListReq
import com.iprism.school.model.Request.StaffListResponse
import com.iprism.school.model.Response.AllCabsResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CabsListActivity : BaseActivity() {

    private lateinit var binding: ActivityCabsListBinding
    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCabsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        cabsList()

    }

    private fun cabsList() {
        showProgress()
        var apiRequest = SchoolStaffReq(auth_token,scl_id,teacherId)
        Log.d("staffList", apiRequest.toString())
        val call: Call<AllCabsResponse> = parentApiService!!.allCabsList(apiRequest)
        call.enqueue(object : Callback<AllCabsResponse> {
            override fun onResponse(call: Call<AllCabsResponse>, response: Response<AllCabsResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        binding.nodata.visibility = View.GONE
                        binding.rvList.visibility = View.VISIBLE

                        val adap1 = CabsListAdapter(this@CabsListActivity, loginApiResponse.response.cabs)
                        binding.rvList.layoutManager = LinearLayoutManager(this@CabsListActivity, LinearLayoutManager.VERTICAL, false)
                        binding.rvList.adapter = adap1
                        adap1.notifyDataSetChanged()

                        adap1.deactiveCallBack = {
                                mydata ->
                            val staffId = mydata.id.toString()
                            var status = ""
                        }
                    }else{
                        binding.nodata.visibility = View.VISIBLE
                        binding.rvList.visibility = View.GONE

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@CabsListActivity, response.message())
                }
            }
            override fun onFailure(call: Call<AllCabsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@CabsListActivity, t.message.toString())
            }
        })
    }


}