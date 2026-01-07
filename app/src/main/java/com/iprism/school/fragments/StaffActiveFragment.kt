package com.iprism.school.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.base.BaseFragment
import com.iprism.school.activities.DaycareReportActivity
import com.iprism.school.activities.EditStaffDetailsActivity
import com.iprism.school.activities.LoginActivity
import com.iprism.school.adapters.DayCareViewListAdapter
import com.iprism.school.adapters.StaffAdapter
import com.iprism.school.databinding.DeactivateStaffDialogBinding
import com.iprism.school.databinding.FragmentStaffActiveBinding
import com.iprism.school.interfaces.OnStaffClickListener
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Response.DayCareViewListResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StaffActiveFragment : BaseFragment() {

    private lateinit var binding: FragmentStaffActiveBinding
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStaffActiveBinding.inflate(inflater, container, false)
        setupStaffAdapter()

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        staffList()

        return binding.root
    }

    private fun setupStaffAdapter() {
        var staffAdapter = StaffAdapter(requireContext())
        binding.activeStaffRv.adapter = staffAdapter
        var linearLayoutManager = LinearLayoutManager(requireContext())
        binding.activeStaffRv.layoutManager = linearLayoutManager
        staffAdapter.setListener(object : OnStaffClickListener{
            override fun onItemClick() {
                var intent = Intent(requireContext(), EditStaffDetailsActivity::class.java)
                startActivity(intent)
            }

            override fun onDeActiveClick() {
                showDeActiveDialog()
            }

            override fun onReActiveClick() {

            }

            override fun onCallClick() {

            }

        })
    }

    private fun showDeActiveDialog() {
        val binding = DeactivateStaffDialogBinding.inflate(layoutInflater)
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(binding.root)
        val dialog = dialogBuilder.create()
       // dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        binding.noBtn.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        binding.yesBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(requireContext(), "User Deactivated Successfully!")
            dialog.dismiss()
        })
        dialog.show()
    }

    private fun staffList() {
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

//                        binding.nodata.visibility = View.GONE
                        binding.activeStaffRv.visibility = View.VISIBLE

                        binding.activeStaffRv.layoutManager = GridLayoutManager(requireContext(),4)
                        val adapter = DayCareViewListAdapter(requireContext(),loginApiResponse.response.daycare)
                        binding.activeStaffRv.adapter = adapter
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