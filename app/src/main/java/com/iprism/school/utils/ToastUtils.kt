package com.iprism.school.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.iprism.school.R

object ToastUtils {

    fun showSuccessCustomToast(context: Context, message: String) {
        val layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.success_custom_toast_layout, null)

        val toastImage = view.findViewById<ImageView>(R.id.toastImage)
        val toastText = view.findViewById<TextView>(R.id.toastText)
        toastText.text = message

        val customToast = Toast(context)
        customToast.view = view
        customToast.duration = Toast.LENGTH_SHORT
        customToast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)

        customToast.show()

        Handler(Looper.getMainLooper()).postDelayed({
            customToast.cancel()
        }, 3500)
    }

    fun showErrorCustomToast(context: Context, message: String) {
        val layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.error_custome_toast_layout, null)

        val toastImage = view.findViewById<ImageView>(R.id.toastImage)
        val toastText = view.findViewById<TextView>(R.id.toastText)
        toastText.text = message

        val customToast = Toast(context)
        customToast.view = view
        customToast.duration = Toast.LENGTH_SHORT
        customToast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)

        customToast.show()

        Handler(Looper.getMainLooper()).postDelayed({
            customToast.cancel()
        }, 3500)
    }

}