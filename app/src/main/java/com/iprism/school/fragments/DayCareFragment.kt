package com.iprism.school.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.school.base.BaseFragment
import com.iprism.school.activities.LoginActivity
import com.iprism.school.adapters.DayCareViewListAdapter
import com.iprism.school.databinding.FragmentDayCareBinding
import com.iprism.school.interfaces.OnDayCareClickListener
import com.iprism.school.model.Request.SchoolStaffReq
import com.iprism.school.model.Response.DayCareViewListResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import com.iprism.school.viewModels.Scl_ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        return binding.root
    }


}