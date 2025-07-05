package com.iprism.school.fragments

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.parentapp.base.BaseFragment
import com.iprism.school.R
import com.iprism.school.activities.DaycareReportActivity
import com.iprism.school.activities.LoginActivity
import com.iprism.school.adapters.DairiesAdapter
import com.iprism.school.adapters.DairiesNewAdapter
import com.iprism.school.adapters.DayCareViewListAdapter
import com.iprism.school.adapters.DayCaresAdapter
import com.iprism.school.databinding.FragmentDayCareBinding
import com.iprism.school.interfaces.OnDayCareClickListener
import com.iprism.school.model.Request.DairyStudentsReq
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Response.Class_studentResponse
import com.iprism.school.model.Response.DayCareViewListResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import com.iprism.school.viewModels.Scl_ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class DayCareFragment : BaseFragment() {

    private lateinit var binding: FragmentDayCareBinding
    private val viewModel: Scl_ViewModel by viewModels()
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDayCareBinding.inflate(inflater, container, false)
//        setupDayCareAdapter()

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        callDaycareviewList()

        return binding.root
    }

    private fun setupDayCareAdapter() {

        var dayCaresAdapter = DayCaresAdapter(requireContext())
        binding.dayCareRv.adapter = dayCaresAdapter
        var linearLayoutManager = GridLayoutManager(requireContext(), 3)
        binding.dayCareRv.layoutManager = linearLayoutManager

        dayCaresAdapter.setupListener(object : OnDayCareClickListener{
            override fun onItemLick(id: String, name: String) {
                var intent = Intent(context, DaycareReportActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("name", name)
                startActivity(intent)
            }
        })
    }


    private fun callDaycareviewList() {
        showProgress()
        var loginApiRequest = SchoolStaffReq(auth_token,scl_id,teacherId)
        Log.d("day_care_Req", loginApiRequest.toString())
        var call: Call<DayCareViewListResponse> = parentApiService!!.daycareViewList(loginApiRequest)
        call.enqueue(object : Callback<DayCareViewListResponse> {
            override fun onResponse(call: Call<DayCareViewListResponse>, response: Response<DayCareViewListResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    var loginApiResponse = response.body()
                    Log.d("class_Students_Response", loginApiResponse.toString())

                    if (loginApiResponse!!.status) {
                        hideProgress()

                        binding.nodata.visibility = View.GONE
                        binding.dayCareRv.visibility = View.VISIBLE

                        binding.dayCareRv.layoutManager = GridLayoutManager(requireContext(),4)
                        val adapter = DayCareViewListAdapter(requireContext(),loginApiResponse.response.daycare)
                        binding.dayCareRv.adapter = adapter
                        adapter.notifyDataSetChanged()

                        adapter.OnItemBtn = {
                                mydata ->
                            var intent = Intent(context, DaycareReportActivity::class.java)
                            intent.putExtra("id", mydata.id.toString())
                            intent.putExtra("name", mydata.name.toString())
                            intent.putExtra("type", mydata.type.toString())
                            intent.putExtra("group_id", mydata.id.toString())
                            startActivity(intent)
                        }

                    } else {
                        hideProgress()
                        ToastUtils.showSuccessCustomToast(requireContext(), loginApiResponse.message.toString())
                        if (loginApiResponse.message.toString() == "Authentication Token Expired"){
                            user!!.storeUserDetails("","","","","","","","","","","","","","","","","","")
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                            activity!!.finish()
                        }
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(requireContext(), "Failed")
                }
            }
            override fun onFailure(call: Call<DayCareViewListResponse>, t: Throwable) {
                hideProgress()
//                ToastUtils.showErrorCustomToast(requireContext(), "Response Failed")
            }
        })
    }

}