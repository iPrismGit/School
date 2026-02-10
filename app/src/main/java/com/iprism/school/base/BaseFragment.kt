package com.iprism.school.base

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.iprism.school.network.StaffApiService
import com.iprism.school.R
import com.iprism.school.utils.NetworkUtil
import com.iprism.school.utils.User

open class BaseFragment : Fragment() {

    var user: User? = null
    lateinit var userDetails: HashMap<String, String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (user == null) {
            user = User(requireContext())
            userDetails = user!!.getNewUserDetails()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
