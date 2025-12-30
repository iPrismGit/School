package com.iprism.school.utils

import android.app.ProgressDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import com.wang.avi.AVLoadingIndicatorView

fun Context.showToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun AVLoadingIndicatorView.showProgress() {
    this.visibility = View.VISIBLE
}

fun AVLoadingIndicatorView.hideProgress() {
    this.visibility = View.GONE
}

fun Context.getUserDetails(): HashMap<String, String?> {
    val user = User(this)
    return user.getUserDetails()
}

fun Context.showProgressDialog(message: String): ProgressDialog {
    val progressDialog = ProgressDialog(this)
    progressDialog.setMessage(message)
    progressDialog.setCancelable(false)
    progressDialog.show()
    return progressDialog
}

fun ProgressDialog.dismissDialog() {
    if (this.isShowing) {
        this.dismiss()
    }
}