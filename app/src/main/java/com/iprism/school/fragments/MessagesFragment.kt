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
        messagesList()

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

            messagesList()

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

            messagesList()

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

            messagesList()

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

            messagesList()

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


//    inboxMessagesMarked = loginApiResponse.response.inbox_messages.map {
//        InboxMessage(it.inbox_message_from, it.message_id)
//    }.toMutableList()
//    Log.d("inboxMessagesMarked", inboxMessagesMarked.toString())

    private fun messagesList() {
        showProgress()
        var apiRequest = InboxMessagesReq(auth_token,inbox_message_type,msg_type,scl_id,"",teacherId)
        Log.d("homeUploadAlbum_Req", apiRequest.toString())
        val call: Call<InboxMessagesResponse> = parentApiService!!.inbox_messages(apiRequest)
        call.enqueue(object : Callback<InboxMessagesResponse> {
            override fun onResponse(call: Call<InboxMessagesResponse>, response: Response<InboxMessagesResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()

                    if (loginApiResponse!!.status == true){

                        binding.nodataTv.visibility = View.GONE
                        binding.messagesRv.visibility = View.VISIBLE

                        inboxMessagesMarked = loginApiResponse.response.inbox_messages.map {
                            mapOf("id" to it.message_id, "from" to it.inbox_message_from)
                        }.toMutableList()

                         jsonFormattedMessages = Gson().toJson(inboxMessagesMarked)

                        Log.d("API_RESPONSE_MSG", jsonFormattedMessages.toString())

                        if (isAdded){
                            val linearLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                            binding.messagesRv.layoutManager = linearLayoutManager
                            val albumsAdapter = MessagesAdapter(requireActivity(), loginApiResponse.response.inbox_messages)
                            binding.messagesRv.adapter = albumsAdapter

                            albumsAdapter.OnItemBtn = {
                                    mydata ->
                                val message_id = mydata.message_id.toString()
                                val inbox_message_from = mydata.inbox_message_from.toString()
                                val intent = Intent(requireActivity(), MessageDetailsActivity::class.java)
                                intent.putExtra("inbox_message_from",inbox_message_from)
                                intent.putExtra("message_id",message_id)
                                startActivity(intent)
                            }

                            albumsAdapter.starBtn = {
                                    mydata ->
                                val message_id = mydata.message_id.toString()
                                val inbox_message_from = mydata.inbox_message_from.toString()
                                val star_msg = mydata.starred_message.toString()
                                var inbox_message_status = ""
                                if (star_msg == ""){
                                    inbox_message_status = "starred"
                                }else{
                                    inbox_message_status = "unstarred"
                                }

                                callMsgUpdate(message_id,inbox_message_from,inbox_message_status)
                            }

                        }
                    }else{
                        binding.nodataTv.visibility = View.VISIBLE
                        binding.messagesRv.visibility = View.GONE
                    }
                } else {
                    binding.nodataTv.visibility = View.VISIBLE
                    binding.messagesRv.visibility = View.GONE

                    hideProgress()
                    ToastUtils.showErrorCustomToast(requireActivity(), response.message())
                }
            }
            override fun onFailure(call: Call<InboxMessagesResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(requireActivity(), t.message.toString())
            }
        })
    }


//    inbox_message_type == "" get all messages not archived,
//    inbox_message_type == "archived" get all archived messages,
//    inbox_message_type == "starred" get all starred messages,
//    inbox_message_type == "read_message" get all unread  messages,

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
                        markALlReadMessages()
                    }else if (selectedText == "Starred Message"){
                        inbox_message_type = "starred"
                        messagesList()
                    }else if (selectedText == "Unread Message"){
                        inbox_message_type = "read_message"
                        messagesList()
                    }else if (selectedText == "Archived Messages"){
                        inbox_message_type = "archived"
                        messagesList()
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

    private fun markALlReadMessages() {
            showProgress()
            var apiRequest = MarkAllReadReq(auth_token,jsonFormattedMessages,scl_id,teacherId)
            Log.d("mark_AllRead_Req", apiRequest.toString())
            val call: Call<SuccessResponsePojo> = parentApiService!!.markALlMessages(apiRequest)
            call.enqueue(object : Callback<SuccessResponsePojo> {
                override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                    if (response.isSuccessful) {
                        val loginApiResponse = response.body()
                        if (loginApiResponse!!.status == true){

                            hideProgress()
                            messagesList()

                        }else{
                            hideProgress()
                        }
                    } else {
                        hideProgress()
                        ToastUtils.showErrorCustomToast(requireActivity(), "Failure")
                    }
                }
                override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(requireActivity(), t.message.toString())
                }
            })
    }


    private fun callMsgUpdate(
        message_id: String,
        inbox_message_from: String,
        inbox_message_status: String
    ) {
        showProgress()
        var apiRequest = Update(auth_token,inbox_message_from,message_id,inbox_message_status,scl_id,teacherId)
        Log.d("msg_Update_Req", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.msgUpdate(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

                        hideProgress()
                        messagesList()

                    }else{
                        hideProgress()
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(requireActivity(), "Failure")
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(requireActivity(), t.message.toString())
            }
        })
    }


}