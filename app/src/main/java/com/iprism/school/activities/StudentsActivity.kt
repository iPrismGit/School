package com.iprism.school.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.adapters.StudentsListAdapter
import com.iprism.school.databinding.ActivityStudentsBinding
import com.iprism.school.model.Request.StudentsListReq
import com.iprism.school.model.Response.StudentListResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentsActivity : BaseActivity() {

    private lateinit var binding : ActivityStudentsBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var message_id: String = ""
    private var student_Type: String = "active"
    private var type: String = ""
    private var student_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        tag = intent.getStringExtra("tag").toString()
        message_id = intent.getStringExtra("message_id").toString()

        binding.backBtn.setOnClickListener {
            val intent = Intent(this@StudentsActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.activeDetailsLl.setOnClickListener {
            student_Type = "active"
            type = ""
            binding.etSearch.text.clear()
            binding.activeDetailsLl.setBackgroundResource(R.color.blue)
            binding.inactiveDetailsLl.setBackgroundResource(R.color.white)

            binding.activeDetailsLl.setTextColor(ContextCompat.getColor(this,R.color.white))
            binding.inactiveDetailsLl.setTextColor(ContextCompat.getColor(this,R.color.black))

            callStudentsList()
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
                callStudentsList()
            }
        }

        binding.inactiveDetailsLl.setOnClickListener {
            student_Type = "inactive"
            type = ""
            binding.etSearch.text.clear()
            binding.activeDetailsLl.setBackgroundResource(R.color.white)
            binding.inactiveDetailsLl.setBackgroundResource(R.color.blue)
            binding.activeDetailsLl.setTextColor(ContextCompat.getColor(this,R.color.black))
            binding.inactiveDetailsLl.setTextColor(ContextCompat.getColor(this,R.color.white))
            callStudentsList()
        }

        binding.plusBtn.setOnClickListener {
            val intent = Intent(this@StudentsActivity, FillSchoolDetailsActivity::class.java)
            intent.putExtra("tag","new")
            startActivity(intent)
        }
        callStudentsList()
    }

    private fun callStudentsList() {
        showProgress()
        var apiRequest = StudentsListReq(auth_token,"",scl_id,"",student_Type,teacherId,type)
        Log.d("student_List", apiRequest.toString())
        val call: Call<StudentListResponse> = parentApiService!!.studentsList(apiRequest)
        call.enqueue(object : Callback<StudentListResponse> {
            override fun onResponse(call: Call<StudentListResponse>, response: Response<StudentListResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        binding.nodata.visibility = View.GONE
                        binding.rvList.visibility = View.VISIBLE

                        val adap1 = StudentsListAdapter(this@StudentsActivity, loginApiResponse.response.students)
                        binding.rvList.layoutManager = LinearLayoutManager(this@StudentsActivity, LinearLayoutManager.VERTICAL, false)
                        binding.rvList.adapter = adap1
                        adap1.notifyDataSetChanged()


                        adap1.OnItemCallBack = {
                                mydata ->
                            val studentId = mydata.id.toString()

                            val intent = Intent(this@StudentsActivity, StudentDetailsActivity::class.java)
                            intent.putExtra("studentId",studentId)
                            intent.putExtra("tag","edit")
                            startActivity(intent)


                            var status = ""
                            if (mydata.status == "1"){
                                status = "deactivate"
                            }else{
                                status = "activate"
                            }
//                            changesStatus(staffId,status)
                        }

//                        adap1.deactiveCallBack = {
//                                mydata ->
//                            val staffId = mydata.id.toString()
//                            var status = ""
//                            if (mydata.status == "1"){
//                                status = "deactivate"
//                            }else{
//                                status = "activate"
//                            }
////                            changesStatus(staffId,status)
//                        }
//
//                        adap1.OnItemCallEdit = {
//                                mydata ->
//                            val staffId = mydata.id.toString()
//                            var status = ""
//                            if (mydata.status == "1"){
//                                status = "deactivate"
//                            }else{
//                                status = "activate"
//                            }
//                            val intent = Intent(this@StudentsActivity, CreateStaffActivity::class.java)
//                            intent.putExtra("staffId",staffId)
//                            intent.putExtra("status",status)
//                            intent.putExtra("tag","edit")
//                            startActivity(intent)
//                        }
                    }else{
                        binding.nodata.visibility = View.VISIBLE
                        binding.rvList.visibility = View.GONE
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@StudentsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<StudentListResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@StudentsActivity, t.message.toString())
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@StudentsActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}