package com.iprism.school.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.base.BaseActivity
import com.iprism.school.adapters.DayCareReportsDetailsListAdapter
import com.iprism.school.databinding.ActivityDaycareReportDetailsBinding
import com.iprism.school.model.Request.DeleteDaycareReportReq
import com.iprism.school.model.Request.ViewDayCareReq
import com.iprism.school.model.Response.DaycareReportResponse
import com.iprism.school.model.Response.ViewDayCareResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaycareReportDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityDaycareReportDetailsBinding
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var studentId: String = ""
    private var selected_date: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaycareReportDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        studentId = intent.getStringExtra("studentId").toString()
        selected_date = intent.getStringExtra("selected_date").toString()

        callReports()

    }

    private fun callReports() {
        showProgress()
        var loginApiRequest = ViewDayCareReq(auth_token,selected_date,scl_id,studentId, teacherId)
        Log.d("report_List_Req", loginApiRequest.toString())
        val call: Call<ViewDayCareResponse> = parentApiService!!.viewDayCare(loginApiRequest)
        call.enqueue(object : Callback<ViewDayCareResponse> {
            override fun onResponse(
                call: Call<ViewDayCareResponse>,
                response: Response<ViewDayCareResponse>) {
                if (response.isSuccessful) {
                    hideProgress()

                    val loginApiResponse = response.body()
                    Log.d("loginApiResponse", loginApiResponse.toString())

                    if (loginApiResponse != null && loginApiResponse.status) {
                        if (loginApiResponse.response.student_daycare_reports.isEmpty()) {
                            binding.nodataTv.visibility = View.VISIBLE
                            binding.dayCareReportsRv.visibility = View.GONE
                        } else {
                            binding.nodataTv.visibility = View.GONE
                            binding.dayCareReportsRv.visibility = View.VISIBLE

                            var dairiesAdapter = DayCareReportsDetailsListAdapter(this@DaycareReportDetailsActivity,loginApiResponse.response.student_daycare_reports)
                            binding.dayCareReportsRv.adapter = dairiesAdapter
                            var layoutManager = LinearLayoutManager(this@DaycareReportDetailsActivity)
                            binding.dayCareReportsRv.layoutManager = layoutManager

                            dairiesAdapter.OnItemBtn = {
                                    mydata ->
                                val reportId = mydata.id.toString()
                                deleteReports(reportId)

                            }


                        }
                    } else {
                        hideProgress()
                        binding.nodataTv.visibility = View.VISIBLE
                        binding.dayCareReportsRv.visibility = View.GONE
                        ToastUtils.showSuccessCustomToast(this@DaycareReportDetailsActivity, loginApiResponse?.message ?: "Error")
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@DaycareReportDetailsActivity, response.message())
                }
            }

            override fun onFailure(call: Call<ViewDayCareResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@DaycareReportDetailsActivity, t.message.toString())
            }
        })
    }


    private fun deleteReports(reportId1: String) {
        showProgress()
        var loginApiRequest = DeleteDaycareReportReq(auth_token,reportId1,scl_id, studentId,teacherId)
        Log.d("report_delete_Req", loginApiRequest.toString())
        val call: Call<DaycareReportResponse> = parentApiService!!.deleteDayCareReport(loginApiRequest)
        call.enqueue(object : Callback<DaycareReportResponse> {
            override fun onResponse(
                call: Call<DaycareReportResponse>,
                response: Response<DaycareReportResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    Log.d("aaa_api_response", loginApiResponse.toString())
                    if (loginApiResponse != null && loginApiResponse.status) {
                        callReports()
                    } else {
                        hideProgress()
                        binding.dayCareReportsRv.visibility = View.GONE
                        ToastUtils.showSuccessCustomToast(this@DaycareReportDetailsActivity, loginApiResponse?.message ?: "Error")
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@DaycareReportDetailsActivity, response.message())
                }
            }

            override fun onFailure(call: Call<DaycareReportResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@DaycareReportDetailsActivity, t.message.toString())
            }
        })
    }


}
