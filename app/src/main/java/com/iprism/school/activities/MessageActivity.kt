package com.iprism.school.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityMessageBinding
import com.iprism.school.databinding.MessageCoonfirmationDialogBinding
import com.iprism.school.utils.ToastUtils

class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleSendMessageBtn()
    }

    private fun handleSendMessageBtn() {
        binding.sendMessageBtn.setOnClickListener(View.OnClickListener {
            showConfirmationDialog()
        })
    }


    private fun showConfirmationDialog() {
        val confirmationBinding = MessageCoonfirmationDialogBinding.inflate(layoutInflater)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(confirmationBinding.root)
        val dialog = dialogBuilder.create()
        dialog.show()
        confirmationBinding.noBtn.setOnClickListener(View.OnClickListener {
           dialog.dismiss()
        })

        confirmationBinding.yesBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Message Sent Successfully!")
            dialog.dismiss()
            finish()
        })
    }

}