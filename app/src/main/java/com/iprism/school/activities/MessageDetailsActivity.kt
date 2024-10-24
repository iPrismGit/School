package com.iprism.school.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityMessageDetailsBinding
import com.iprism.school.databinding.AddMoreBottomSheetLayoutBinding
import com.iprism.school.databinding.MessageDeleteBottomSheetBinding
import com.iprism.school.utils.ToastUtils

class MessageDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageDetailsBinding
    private var tag: String = ""
    private var messageId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageDetailsBinding.inflate(layoutInflater)
        tag = intent.getStringExtra("tag").toString()
        setContentView(binding.root)
        setupView(tag)
        handleDeleteIv()
        handleApproveBtn()
        handleRejectBtn()
        handleForwordBtn()
        handelInfoIv()
    }

    private fun handelInfoIv() {
        binding.infoIv.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, MessageInfoActivity::class.java)
            intent.putExtra("messageId", messageId)
            startActivity(intent)
        })
    }

    private fun handleForwordBtn() {
        binding.forwordIv.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, MessageActivity::class.java)
            startActivity(intent)
        })
    }

    private fun handleRejectBtn() {
        binding.rejectBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Attendance Rejected Successfully")
            finish()
        })
    }

    private fun handleApproveBtn() {
        binding.approveBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Attendance Approved Successfully")
            finish()
        })
    }

    private fun handleDeleteIv() {
        binding.deleteIv.setOnClickListener(View.OnClickListener {
            showDeleteBottomSheet()
        })
    }

    private fun setupView(tag : String) {
        if (tag.equals("msgInbox", true)) {
            binding.activityTitleTxt.text = "Messages"
            binding.uploadIv.visibility = View.VISIBLE
            binding.forwordIv.visibility = View.VISIBLE
            binding.deleteIv.visibility = View.GONE
            binding.infoIv.visibility = View.GONE
            binding.buttonsLo.visibility = View.GONE
            binding.sndMessageLo.visibility = View.VISIBLE
        } else if (tag.equals("sent", true)) {
            binding.activityTitleTxt.text = "Sent Messages"
            binding.deleteIv.visibility = View.VISIBLE
            binding.uploadIv.visibility = View.GONE
            binding.forwordIv.visibility = View.VISIBLE
            binding.infoIv.visibility = View.VISIBLE
            binding.buttonsLo.visibility = View.GONE
            binding.sndMessageLo.visibility = View.GONE
        } else if (tag.equals("scheduled", true)) {
            binding.activityTitleTxt.text = "Scheduled Messages"
            binding.deleteIv.visibility = View.VISIBLE
            binding.uploadIv.visibility = View.GONE
            binding.forwordIv.visibility = View.GONE
            binding.infoIv.visibility = View.GONE
            binding.buttonsLo.visibility = View.GONE
            binding.sndMessageLo.visibility = View.GONE
        }
    }

    private fun showDeleteBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val deleteBinding = MessageDeleteBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(deleteBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.rounded_bottom_sheet_background)
            deleteBinding.okBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
                ToastUtils.showSuccessCustomToast(this, "Message Deleted Successfully")
                finish()
            })

            deleteBinding.crossIv.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })

            deleteBinding.cancelBtn.setOnClickListener(View.OnClickListener {
                bottomSheetDialog.dismiss()
            })
        }
        bottomSheetDialog.show()
    }
}