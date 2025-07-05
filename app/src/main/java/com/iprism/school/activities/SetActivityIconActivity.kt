package com.iprism.school.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.adapters.ActivityIconsAdapter
import com.iprism.school.adapters.SetActivityIconsAdapter
import com.iprism.school.databinding.ActivitySetIconBinding
import com.iprism.school.model.Request.CalenderDeleteReq
import com.iprism.school.model.Request.DaycareActivityIconUpdateReq
import com.iprism.school.model.Response.ActivityIconsResponse
import com.iprism.school.model.Response.DaycareActivityesResponse
import com.iprism.school.model.Response.DaycareReportResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetActivityIconActivity : BaseActivity() {

    private lateinit var binding: ActivitySetIconBinding
    private lateinit var crossImg: ImageView
    private lateinit var activityIconsRv: RecyclerView

    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetIconBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
//        setupActivityIconsAdapter()

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        callActivityyes()

    }

    private fun setupActivityIconsAdapter() {

    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun updateActivityIconsDialog(
        activity_Id: String,
        activityIcon_Id: String,
        dialog: AlertDialog
    ) {
        showProgress()
        var loginApiRequest = DaycareActivityIconUpdateReq(activityIcon_Id,activity_Id,auth_token,scl_id,teacherId)
        Log.d("activity_Icons_Req", loginApiRequest.toString())
        val call: Call<DaycareReportResponse> = parentApiService!!.updateIconsDaycare(loginApiRequest)
        call.enqueue(object : Callback<DaycareReportResponse> {
            override fun onResponse(
                call: Call<DaycareReportResponse>,
                response: Response<DaycareReportResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    Log.d("loginApiResponse", loginApiResponse.toString())

                    if (loginApiResponse != null && loginApiResponse.status) {

                        callActivityyes()

                        dialog.dismiss()

                    } else {
                        hideProgress()
                        binding.nodataTv.visibility = View.VISIBLE
                        binding.activityIconsRv.visibility = View.GONE
                        ToastUtils.showSuccessCustomToast(this@SetActivityIconActivity, loginApiResponse?.message ?: "Error")
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@SetActivityIconActivity, response.message())
                }
            }

            override fun onFailure(call: Call<DaycareReportResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@SetActivityIconActivity, t.message.toString())
            }
        })

    }


    private fun callActivityyes() {
        showProgress()
        var loginApiRequest = CalenderDeleteReq( auth_token,"",scl_id,teacherId)
        Log.d("activity_Icons_Req", loginApiRequest.toString())
        val call: Call<DaycareActivityesResponse> = parentApiService!!.activity_viewIconsDaycare(loginApiRequest)
        call.enqueue(object : Callback<DaycareActivityesResponse> {
            override fun onResponse(
                call: Call<DaycareActivityesResponse>,
                response: Response<DaycareActivityesResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    Log.d("loginApiResponse", loginApiResponse.toString())

                    if (loginApiResponse != null && loginApiResponse.status) {
                        if (loginApiResponse.response.activities.isEmpty()) {
                            binding.nodataTv.visibility = View.VISIBLE
                            binding.activityIconsRv.visibility = View.GONE
                        } else {
                            binding.nodataTv.visibility = View.GONE
                            binding.activityIconsRv.visibility = View.VISIBLE

                            var dairiesAdapter = SetActivityIconsAdapter(this@SetActivityIconActivity,loginApiResponse.response.activities)
                            binding.activityIconsRv.adapter = dairiesAdapter
                            var layoutManager = LinearLayoutManager(this@SetActivityIconActivity)
                            binding.activityIconsRv.layoutManager = layoutManager

                            dairiesAdapter.OnItemBtn = {
                                    mydata ->
                                val activity_Id = mydata.id.toString()
                                callSetIconsActivityyes(activity_Id)
                            }
                        }
                    } else {
                        hideProgress()
                        binding.nodataTv.visibility = View.VISIBLE
                        binding.activityIconsRv.visibility = View.GONE
                        ToastUtils.showSuccessCustomToast(this@SetActivityIconActivity, loginApiResponse?.message ?: "Error")
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@SetActivityIconActivity, response.message())
                }
            }

            override fun onFailure(call: Call<DaycareActivityesResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@SetActivityIconActivity, t.message.toString())
            }
        })
    }


    private fun callSetIconsActivityyes(activity_Id: String) {
        showProgress()
        var loginApiRequest = CalenderDeleteReq( auth_token,"",scl_id,teacherId)
        Log.d("activity_Icons_Req", loginApiRequest.toString())
        val call: Call<ActivityIconsResponse> = parentApiService!!.viewIconsDaycare(loginApiRequest)
        call.enqueue(object : Callback<ActivityIconsResponse> {
            override fun onResponse(
                call: Call<ActivityIconsResponse>,
                response: Response<ActivityIconsResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    Log.d("loginApiResponse", loginApiResponse.toString())

                    val dialogView = layoutInflater.inflate(R.layout.activity_icons_layout, null)
                    val dialogBuilder = AlertDialog.Builder(this@SetActivityIconActivity)
                    dialogBuilder.setView(dialogView)
                    val dialog = dialogBuilder.create()
                    dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
                    crossImg = dialogView.findViewById<View>(R.id.cross_iv) as ImageView

                    activityIconsRv = dialogView.findViewById<View>(R.id.activity_icons_rv) as RecyclerView
                    var activityIconsAdapter = ActivityIconsAdapter(this@SetActivityIconActivity,loginApiResponse!!.response.activities)
                    activityIconsRv.adapter = activityIconsAdapter
                    var linearLayout = GridLayoutManager(this@SetActivityIconActivity, 3)
                    activityIconsRv.layoutManager = linearLayout

                    activityIconsAdapter.OnItemBtn = {
                        mydata ->
                        val activityIcon_Id = mydata.id.toString()
                        updateActivityIconsDialog(activity_Id,activityIcon_Id,dialog)
                    }
                    crossImg.setOnClickListener(View.OnClickListener {
                        dialog.dismiss()
                    })

                    dialog.show()
                }else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@SetActivityIconActivity, response.message())
                }
            }
            override fun onFailure(call: Call<ActivityIconsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@SetActivityIconActivity, t.message.toString())
            }
        })
    }




}


