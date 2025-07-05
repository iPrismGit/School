package com.iprism.parentapp.base

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
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.iprism.parentapp.network.StaffApiService
import com.iprism.school.R
import com.iprism.school.network.StaffApi
import com.iprism.school.utils.NetworkUtil
import com.iprism.school.utils.User
import com.tuyenmonkey.mkloader.MKLoader

open class BaseFragment : Fragment() {

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
            user = User(requireContext())
            userDetails = user!!.getUserDetails()
        }
        // Network receiver to listen for changes in connectivity
        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (!NetworkUtil.isConnected(context)) {
                    activity?.runOnUiThread {
                        showNetworkPopup() // Ensure UI updates happen on the main thread
                    }
                } else {
                    activity?.runOnUiThread {
                        hideNetworkPopup() // Ensure UI updates happen on the main thread
                    }
                }
            }
        }

        // Register the network receiver
        requireActivity().registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        // Periodic network check to handle edge cases
        networkCheckRunnable = object : Runnable {
            override fun run() {
                if (!NetworkUtil.isConnected(requireContext())) {
                    activity?.runOnUiThread {
                        showNetworkPopup() // Ensure UI updates happen on the main thread
                    }
                } else {
                    activity?.runOnUiThread {
                        hideNetworkPopup() // Ensure UI updates happen on the main thread
                    }
                }
                handler.postDelayed(this, 3000) // Check every 3 seconds
            }
        }
        handler.post(networkCheckRunnable as Runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(networkReceiver)
        networkCheckRunnable?.let { handler.removeCallbacks(it) }
    }

    private fun showNetworkPopup() {
        if (alertDialog == null || !alertDialog!!.isShowing) {
            val builder = AlertDialog.Builder(requireContext())
            val inflater = LayoutInflater.from(requireContext())
            val dialogView = inflater.inflate(R.layout.popup_newtwork_check, null)
            builder.setView(dialogView)
            builder.setCancelable(false)

            val retryButton = dialogView.findViewById<Button>(R.id.retryButton)
            retryButton.setOnClickListener {
                if (NetworkUtil.isConnected(requireContext())) {
                    hideNetworkPopup()
                }
            }

            alertDialog = builder.create()
            alertDialog!!.show()
        }
    }

    private fun hideNetworkPopup() {
        if (alertDialog != null && alertDialog!!.isShowing) {
            alertDialog!!.dismiss()
        }
    }

    private fun setupIGienApiService() {
        val parentApi = StaffApi()
        parentApiService = parentApi.createParentApiService()
    }


    protected fun showProgress(progress: MKLoader) {
        progress.visibility = View.VISIBLE
    }

    protected fun hideProgress(progress: MKLoader) {
        progress.visibility = View.GONE
    }


    //handle the progressbar
    fun showProgress() {
        try {
            pDialog = ProgressDialog(activity,R.style.TransparentProgressDialog)
            // pDialog = new ProgressDialog(context);
            pDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            pDialog!!.setIndeterminate(true)
            pDialog!!.setCancelable(false)
            pDialog!!.show()
            pDialog!!.setContentView(R.layout.progressxml)
            pDialog!!.setCanceledOnTouchOutside(false)
            pDialog!!.show()
        } catch (e: Exception) {
            Log.d("AlertDialog", "Progress dialog can not be shown")
        }
    }

    fun hideProgress() {
        if(pDialog!=null&& pDialog!!.isShowing()){
            pDialog!!.dismiss();
        }
    }

}
