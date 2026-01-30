package com.iprism.school.activities.Messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.school.base.BaseActivity
import com.iprism.school.adapters.SentMessagesAdapter
import com.iprism.school.databinding.ActivitySentMessagesBinding
import com.iprism.school.model.Request.MessagesTypeResponse
import com.iprism.school.model.Request.MessagesTypesReq
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SentMessagesActivity : BaseActivity() {

    private lateinit var binding: ActivitySentMessagesBinding
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var emp_designation: String = ""
    private var emp_name: String = ""

    private var tag: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySentMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.ID].toString()
        auth_token = userDetails[User.AUTH_TOKEN].toString()
        scl_id = userDetails[User.SCHOOL_ID].toString()

        tag = intent.getStringExtra("tag").toString()

        if (tag == ""){
            binding.activityTitleTxt.text = "Sent messages"
        }else{
            binding.activityTitleTxt.text = "Scheduled messages"
        }

        emp_name = userDetails[User.EMP_NAME].toString()
        emp_designation = userDetails[User.EMP_DESIGNATION].toString()

        messagesList()
    }

    private fun messagesList() {
        showProgress()
        var apiRequest = MessagesTypesReq(auth_token,tag,scl_id,teacherId)
        Log.d("messagesTypes_Req", apiRequest.toString())
        val call: Call<MessagesTypeResponse> = parentApiService!!.msgSend_Archived(apiRequest)
        call.enqueue(object : Callback<MessagesTypeResponse> {
            override fun onResponse(call: Call<MessagesTypeResponse>, response: Response<MessagesTypeResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()

                    if (loginApiResponse!!.status == true){

                        binding.nodataTv.visibility = View.GONE
                        binding.messagesRv.visibility = View.VISIBLE

                            val linearLayoutManager = LinearLayoutManager(this@SentMessagesActivity, LinearLayoutManager.VERTICAL, false)
                            binding.messagesRv.layoutManager = linearLayoutManager
                            val albumsAdapter = SentMessagesAdapter(this@SentMessagesActivity, loginApiResponse.response.sent_messages)
                            binding.messagesRv.adapter = albumsAdapter

                            albumsAdapter.OnItemBtn = {
                                    mydata ->
                                val message_id = mydata.id.toString()
                                val inbox_message_from = mydata.sent_from.toString()
//                                val intent = Intent(this@SentMessagesActivity, Msg_DetailPageActivity::class.java)
//                                intent.putExtra("inbox_message_from",inbox_message_from)
//                                intent.putExtra("message_id",message_id)
//                                intent.putExtra("tag",tag)
//                                startActivity(intent)
                            }

                    }else{
                        binding.nodataTv.visibility = View.VISIBLE
                        binding.messagesRv.visibility = View.GONE
                    }
                } else {
                    binding.nodataTv.visibility = View.VISIBLE
                    binding.messagesRv.visibility = View.GONE

                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@SentMessagesActivity, response.message())
                }
            }
            override fun onFailure(call: Call<MessagesTypeResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@SentMessagesActivity, t.message.toString())
            }
        })
    }


}