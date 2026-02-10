package com.iprism.school.base

import android.app.Activity
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
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.iprism.school.network.StaffApiService
import com.iprism.school.R
import com.iprism.school.utils.NetworkUtil
import com.iprism.school.utils.User
open class BaseActivity : AppCompatActivity() {

    var user: User? = null
    lateinit var userDetails: HashMap<String, String?>
    private var alertDialog: AlertDialog? = null
    private var networkReceiver: BroadcastReceiver? = null
    private val handler = Handler()
    private var networkCheckRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (user == null) {
            user = User(this)
           // userDetails = user!!.getUserDetails()
            userDetails = user!!.getNewUserDetails()
        }

    }

    protected fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(networkReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        networkCheckRunnable?.let { handler.removeCallbacks(it) }
    }

}
