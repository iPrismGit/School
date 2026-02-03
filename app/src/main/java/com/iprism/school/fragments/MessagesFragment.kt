package com.iprism.school.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.iprism.school.base.BaseFragment
import com.iprism.school.R
import com.iprism.school.activities.Messages.MessageActivity
import com.iprism.school.activities.Messages.MessageDetailsActivity
import com.iprism.school.adapters.MessagesAdapter
import com.iprism.school.databinding.FragmentMessagesBinding
import com.iprism.school.model.Request.InboxMessagesReq
import com.iprism.school.model.Request.MarkAllReadReq
import com.iprism.school.model.Request.Update
import com.iprism.school.model.Response.InboxMessagesResponse
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessagesFragment : BaseFragment() {

    private lateinit var binding: FragmentMessagesBinding
    private var tag: String = ""

    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var msg_type: String = "all"
    private var inbox_message_type: String = ""

    var jsonFormattedMessages: String = ""

//    private var inboxMessagesMarked: MutableList<InboxMessage> = mutableListOf()

    var inboxMessagesMarked: MutableList<Map<String, String>> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMessagesBinding.inflate(inflater)

        tag = arguments?.getString("tag").toString()
        setupFragmentSettings(tag)
        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()
        handleMessageBtn()
        handleClick()

        binding.dotsImg.setOnClickListener {
            showSingleSelectDialog()
            Log.d("newwwwwwww","20255555")
        }
        return binding.root
    }

    private fun handleClick() {

        binding.allTv.setOnClickListener {
            msg_type = "all"

            binding.allTv.setBackgroundResource(R.drawable.color_bg)
            binding.allTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

            binding.parentTv.setBackgroundResource(R.drawable.edit_text_bg)
            binding.parentTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

            binding.staffTv.setBackgroundResource(R.drawable.edit_text_bg)
            binding.staffTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

            binding.systemTv.setBackgroundResource(R.drawable.edit_text_bg)
            binding.systemTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

        }

        binding.parentTv.setOnClickListener {
            msg_type = "parent"
            binding.allTv.setBackgroundResource(R.drawable.edit_text_bg)
            binding.allTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

            binding.parentTv.setBackgroundResource(R.drawable.color_bg)
            binding.parentTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

            binding.staffTv.setBackgroundResource(R.drawable.edit_text_bg)
            binding.staffTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

            binding.systemTv.setBackgroundResource(R.drawable.edit_text_bg)
            binding.systemTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

        }

        binding.staffTv.setOnClickListener {
            msg_type = "staff"
            binding.allTv.setBackgroundResource(R.drawable.edit_text_bg)
            binding.allTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

            binding.parentTv.setBackgroundResource(R.drawable.edit_text_bg)
            binding.parentTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

            binding.staffTv.setBackgroundResource(R.drawable.color_bg)
            binding.staffTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

            binding.systemTv.setBackgroundResource(R.drawable.edit_text_bg)
            binding.systemTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

        }

        binding.systemTv.setOnClickListener {
            msg_type = "system"
            binding.allTv.setBackgroundResource(R.drawable.edit_text_bg)
            binding.allTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

            binding.parentTv.setBackgroundResource(R.drawable.edit_text_bg)
            binding.parentTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

            binding.staffTv.setBackgroundResource(R.drawable.edit_text_bg)
            binding.staffTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

            binding.systemTv.setBackgroundResource(R.drawable.color_bg)
            binding.systemTv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

        }
    }

    private fun setupFragmentSettings(tag: String) {
        if (tag.equals("msg", true)) {
            binding.textView10.text = "Messages"
        } else if (tag.equals("msgInbox", true)) {
            binding.textView10.text = "Messages"
        } else if (tag.equals("sent", true)) {
            binding.textView10.text = "Sent Messages"
        } else if (tag.equals("scheduled", true)) {
            binding.textView10.text = " Scheduled Messages"
        }
    }

    private fun handleMessageBtn() {
        binding.messageBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, MessageActivity::class.java))
        })
    }

    private fun showSingleSelectDialog() {
        val options = arrayOf("Mark all as read", "Starred Message", "Unread Message",
            "Archived Messages","Change Signature")
        var selectedOptionIndex = -1 // Default: No selection

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Select an Option")
            .setSingleChoiceItems(options, selectedOptionIndex) { _, which ->
                selectedOptionIndex = which // Store selected index
            }
            .setPositiveButton("OK") { _, _ ->
                if (selectedOptionIndex != -1) {
                    val selectedText = options[selectedOptionIndex]
                    if (selectedText == "Mark all as read"){
                        inbox_message_type = ""
                    }else if (selectedText == "Starred Message"){
                        inbox_message_type = "starred"
                    }else if (selectedText == "Unread Message"){
                        inbox_message_type = "read_message"

                    }else if (selectedText == "Archived Messages"){
                        inbox_message_type = "archived"

                    }else if (selectedText == "Change Signature"){
//                        inbox_message_type = ""
                    }
//                    binding.selectedOptionTextView.text = selectedText // Update UI with selected option
                }
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .create()

        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }



}