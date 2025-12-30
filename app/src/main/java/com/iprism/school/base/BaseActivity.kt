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
import com.iprism.school.network.StaffApi
import com.iprism.school.utils.NetworkUtil
import com.iprism.school.utils.User
open class BaseActivity : AppCompatActivity() {

    protected var parentApiService: StaffApiService? = null
    var user: User? = null
    lateinit var userDetails: HashMap<String, String?>
    private var alertDialog: AlertDialog? = null
    private var networkReceiver: BroadcastReceiver? = null
    private val handler = Handler()
    private var networkCheckRunnable: Runnable? = null

    private var pDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupIGienApiService()
        if (user == null) {
            user = User(this)
            userDetails = user!!.getUserDetails()
        }

        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (!NetworkUtil.isConnected(context)) {
                    runOnUiThread {
                        showNetworkPopup()
                    }
                } else {
                    runOnUiThread {
                        hideNetworkPopup()
                    }
                }
            }
        }

        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        networkCheckRunnable = object : Runnable {
            override fun run() {
                if (!NetworkUtil.isConnected(this@BaseActivity)) {
                    runOnUiThread {
                        showNetworkPopup()
                    }
                } else {
                    runOnUiThread {
                        hideNetworkPopup()
                    }
                }
                handler.postDelayed(this, 4000)
            }
        }
        handler.post(networkCheckRunnable as Runnable)
    }

    protected fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setupIGienApiService() {
        val parentApi = StaffApi()
        parentApiService = parentApi.createParentApiService()
    }

    protected fun isConnected(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
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

    private fun showNetworkPopup() {
        if (alertDialog == null || !alertDialog!!.isShowing()) {
            val builder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView: View = inflater.inflate(R.layout.popup_newtwork_check, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            val retryButton = dialogView.findViewById<Button>(R.id.retryButton)
            retryButton.setOnClickListener {
                if (NetworkUtil.isConnected(this@BaseActivity)) {
                    hideNetworkPopup()
                }
            }
            alertDialog = builder.create()
            alertDialog!!.show()
        }
    }

    private fun hideNetworkPopup() {
        if (alertDialog != null && alertDialog!!.isShowing()) {
            alertDialog!!.dismiss()
        }
    }


    //handle the progressbar
    fun showProgress() {
        try {
            if (pDialog == null) {
                pDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
                pDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                pDialog!!.setIndeterminate(true)
                pDialog!!.setCancelable(false)
            }
            if (!pDialog!!.isShowing) {
                pDialog!!.show()
                pDialog!!.setContentView(R.layout.progressxml)
            }
        } catch (e: Exception) {
            Log.d("AlertDialog", "Progress dialog can not be shown")
        }
    }

    fun hideProgress() {
        if (pDialog != null && pDialog!!.isShowing) {
            pDialog!!.dismiss()
        }
    }


    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
