package com.iprism.school.fragments

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.parentapp.base.BaseFragment
import com.iprism.school.activities.LoginActivity
import com.iprism.school.activities.circular.SingleConsentActivity
import com.iprism.school.adapters.ConsentsAdapter
import com.iprism.school.databinding.FragmentActiveConsentsBinding
import com.iprism.school.model.Request.ConsentsListReq
import com.iprism.school.utils.User
import com.iprism.school.viewModels.Scl_ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class ActiveConsentsFragment : BaseFragment() {

    private lateinit var binding: FragmentActiveConsentsBinding
    private val viewModel: Scl_ViewModel by viewModels()

    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private var formattedDateString: String = ""
    private val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var calendar = Calendar.getInstance()
    private lateinit var adapter: ConsentsAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    private var displayedDate = LocalDate.now() // e.g., 2025-02-23

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentActiveConsentsBinding.inflate(inflater, container, false)

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        setDate()
        handleRightBtn()
        hanldeLeftBtn()
        updateMonthYearText()

        return binding.root
    }

    private fun setDate() {
        val calendar: java.util.Calendar = java.util.Calendar.getInstance()
        val sdf = java.text.SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val sdfString = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = sdf.format(calendar.time)
        formattedDateString  = sdfString.format(calendar.time)
        Log.d("dateFormatString", formattedDateString)
        binding.dateTxt.text = formattedDate
        consentList(formattedDateString)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun hanldeLeftBtn() {
        binding.leftArrowIv.setOnClickListener(View.OnClickListener {
            changeDate(-1)
            displayedDate = displayedDate.minusMonths(1)
            updateMonthYearText()
        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleRightBtn() {
        binding.rightArrowIv.setOnClickListener(View.OnClickListener {
            changeDate(1)
            displayedDate = displayedDate.plusMonths(1)
            updateMonthYearText()
        })
    }


    private fun changeDate(days: Int) {
        calendar.add(Calendar.MONDAY, days)
        var dateFormatString = simpleDateFormat.format(calendar.time)
        Log.d("dateFormatString", dateFormatString)
        consentList(dateFormatString)
    }


    private fun consentList(formattedDateString: String) {
        showProgress()
        var apiRequest = ConsentsListReq(auth_token,formattedDateString,scl_id,teacherId)
//        var apiRequest = ConsentsListReq(auth_token,"2025-02-10",scl_id,teacherId)
        Log.d("consentListReq", apiRequest.toString())
        viewModel.consentList(apiRequest).observe(requireActivity(), Observer { response ->
            if (response != null && response.status == true) {
                hideProgress()
                Log.d("calenderListResponse", response.toString())

                binding.noDataTv.visibility = View.GONE
                binding.activeConsentsRv.visibility = View.VISIBLE

                adapter = ConsentsAdapter(requireActivity(),response.response.consent_details ?: emptyList())
                binding.activeConsentsRv.adapter = adapter
                var layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
                binding.activeConsentsRv.layoutManager = layoutManager

                adapter.OnItemCallPic = {
                        mydata ->
                    val consentId = mydata.id.toString()
                    val intent = Intent(requireActivity(), SingleConsentActivity::class.java)
                    intent.putExtra("consentId",consentId)
                    startActivity(intent)
                }

            } else {
                hideProgress()
                binding.noDataTv.visibility = View.VISIBLE
                binding.activeConsentsRv.visibility = View.GONE
                if (response!!.message.toString() == "Authentication Token Expired"){
                    user!!.storeUserDetails("","","","","",""
                        ,"","","",""
                        ,"","","","",""
                        ,"","","")
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }else{

                }
            }
        })
    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateMonthYearText() {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        binding.dateTxt.text = displayedDate.format(formatter)
    }


}