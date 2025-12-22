package com.iprism.school.activities.Messages

import android.os.Bundle
import android.util.Log
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityMsgDetailPageBinding
import com.iprism.school.model.Request.SingleMsgDetailsReq
import com.iprism.school.model.Response.SingleMsgDetailsResponse
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Msg_DetailPageActivity : BaseActivity() {

    private lateinit var binding: ActivityMsgDetailPageBinding
    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var message_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMsgDetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        tag = intent.getStringExtra("tag").toString()
        message_id = intent.getStringExtra("message_id").toString()

        messagesDetails()
    }


    private fun messagesDetails() {
        showProgress()
        var apiRequest = SingleMsgDetailsReq(auth_token,tag,scl_id,message_id,teacherId)
        Log.d("singleMsgDetails_Req", apiRequest.toString())
        val call: Call<SingleMsgDetailsResponse> = parentApiService!!.singleMsgView(apiRequest)
        call.enqueue(object : Callback<SingleMsgDetailsResponse> {
            override fun onResponse(call: Call<SingleMsgDetailsResponse>, response: Response<SingleMsgDetailsResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){

//                        Glide.with(this@Msg_DetailPageActivity)
//                            .load(Constants.IMAGES_URL+loginApiResponse.response.teacher_details.in)
//                            .placeholder(R.drawable.baseline_image)
//                            .into(binding.imgs)

                        binding.subjectTv.text  = loginApiResponse.response.sent_messages[0].subject.toString()
                        binding.dateTv.text  = loginApiResponse.response.sent_messages[0].date.toString()
                        binding.fromTv.text  =   loginApiResponse.response.sent_messages[0].sent_from.toString()
                        binding.messageTv.text  = "Message : "+  loginApiResponse.response.sent_messages[0].message
                        binding.usersTv.text  = loginApiResponse.response.sent_messages[0].staff_names.toString()

//                        Glide.with(this@MessageDetailsActivity)
//                            .load(Constants.IMAGES_URL+loginApiResponse.response.inbox_message[0].images)
//                            .placeholder(R.drawable.baseline_image)
//                            .into(binding.msgImages)


                    }else{
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@Msg_DetailPageActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SingleMsgDetailsResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@Msg_DetailPageActivity, t.message.toString())
            }
        })
    }



}

