package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.base.BaseActivity
import com.iprism.school.adapters.GroupsAdapter
import com.iprism.school.databinding.ActivityGroupsBinding
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Response.GroupsListResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupsActivity : BaseActivity() {

    private lateinit var binding: ActivityGroupsBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var student_Type: String = "active"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        binding.backimg.setOnClickListener {
            val intent = Intent(this@GroupsActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        handleAddBtn()

        callStudentsList()
    }

    private fun handleAddBtn() {
        binding.addBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, CreateGroupActivity::class.java))
        })
    }


    private fun callStudentsList() {
        showProgress()
        var apiRequest = SchoolStaffReq(auth_token,scl_id,teacherId)
        Log.d("student_List", apiRequest.toString())
        val call: Call<GroupsListResponse> = parentApiService!!.groupList(apiRequest)
        call.enqueue(object : Callback<GroupsListResponse> {
            override fun onResponse(call: Call<GroupsListResponse>, response: Response<GroupsListResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

//                        binding.nodata.visibility = View.GONE
                        binding.groupsRv.visibility = View.VISIBLE

                        val adap1 = GroupsAdapter(this@GroupsActivity, loginApiResponse.response.groups)
                        binding.groupsRv.layoutManager = LinearLayoutManager(this@GroupsActivity, LinearLayoutManager.VERTICAL, false)
                        binding.groupsRv.adapter = adap1
                        adap1.notifyDataSetChanged()


                        adap1.OnItemCallBack = {
                                mydata ->
                            val groupId = mydata.id.toString()
                            val intent = Intent(this@GroupsActivity, GroupDetailsActivity::class.java)
                            intent.putExtra("groupId",groupId)
                            intent.putExtra("tag","edit")
                            startActivity(intent)
                        }
                    }else{
//                        binding.nodata.visibility = View.VISIBLE
                        binding.groupsRv.visibility = View.GONE
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@GroupsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<GroupsListResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@GroupsActivity, t.message.toString())
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@GroupsActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()

    }

}