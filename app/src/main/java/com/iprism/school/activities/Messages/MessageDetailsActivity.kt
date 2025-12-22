package com.iprism.school.activities.Messages

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.HomeActivity
import com.iprism.school.activities.MessageInfoActivity
import com.iprism.school.adapters.ReplayImageAdapter
import com.iprism.school.databinding.ActivityMessageDetailsBinding
import com.iprism.school.databinding.MessageDeleteBottomSheetBinding
import com.iprism.school.model.Request.InboxMessageReplyReq
import com.iprism.school.model.Request.InboxSingleMsgReq
import com.iprism.school.model.Request.Update
import com.iprism.school.model.Response.InboxSingleMsgResponse
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.Constants
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import com.iprism.school.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

class MessageDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityMessageDetailsBinding
    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    private var message_id: String = ""
    private var inbox_message_from: String = ""
    private var msg_type: String = "all"

    lateinit var resultLauncher: ActivityResultLauncher<Uri>
    lateinit var resultLaunchergallery: ActivityResultLauncher<Intent>

    private var commaSeparatedBase64 : String? = null
    private lateinit var imageAdapter: ReplayImageAdapter
    private val imageUris = mutableListOf<Uri>()
    private lateinit var photoUri: Uri

    private var attachment_type : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        tag = intent.getStringExtra("tag").toString()
        message_id = intent.getStringExtra("message_id").toString()
        inbox_message_from = intent.getStringExtra("inbox_message_from").toString()

        setupView(tag)
        handleDeleteIv()
        handleSendMessageBtn()
//        handleApproveBtn()
//        handleRejectBtn()
        handleForwordBtn()
        handelInfoIv()

        messagesDetails()

        binding.backIv.setOnClickListener {
            val intent = Intent(this@MessageDetailsActivity, HomeActivity::class.java)
            intent.putExtra("tag","msgInbox")
            startActivity(intent)
            finish()
        }

        binding.attachmentImg.setOnClickListener {
            selectImage()
        }

        binding.imagesRv.layoutManager = GridLayoutManager(this,5)
        imageAdapter = ReplayImageAdapter(imageUris) { uri ->
            imageAdapter.deleteImage(uri) }
        binding.imagesRv.adapter = imageAdapter
        imageAdapter.notifyDataSetChanged()
    }

    private fun handleSendMessageBtn() {
        binding.sendImg.setOnClickListener(View.OnClickListener {
            convertImagesToBase64()
            if (binding.messageInput.text.toString() == "" || binding.messageInput.text.toString() == null) {
                ToastUtils.showSuccessCustomToast(this@MessageDetailsActivity, "Enter Message")
            } else {
                callCreateMSG()
            }
        })
    }

    private fun callCreateMSG() {
        showProgress()
        var apiRequest = InboxMessageReplyReq(
            commaSeparatedBase64.toString(),auth_token,message_id,
            msg_type,scl_id, teacherId,binding.messageInput.text.toString())
        Log.d("createNew_Req", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.replayInboxMsg(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(
                call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    ToastUtils.showSuccessCustomToast(this@MessageDetailsActivity,loginApiResponse!!.message.toString())
                    val intent = Intent(this@MessageDetailsActivity, HomeActivity::class.java)
                    intent.putExtra("tag","msgInbox")
                    startActivity(intent)
                    finish()

                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@MessageDetailsActivity, "Failed")
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@MessageDetailsActivity, "Response Failed")
            }
        })
    }


    private fun handelInfoIv() {
        binding.infoIv.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, MessageInfoActivity::class.java)
            intent.putExtra("messageId", message_id)
            startActivity(intent)
        })
    }

    private fun handleForwordBtn() {
        binding.forwordIv.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, MessageActivity::class.java)
            startActivity(intent)
        })
    }

//    private fun handleRejectBtn() {
//        binding.rejectBtn.setOnClickListener(View.OnClickListener {
//            ToastUtils.showSuccessCustomToast(this, "Attendance Rejected Successfully")
//            finish()
//        })
//    }
//
//    private fun handleApproveBtn() {
//        binding.approveBtn.setOnClickListener(View.OnClickListener {
//            ToastUtils.showSuccessCustomToast(this, "Attendance Approved Successfully")
//            finish()
//        })
//    }

    private fun handleDeleteIv() {
//        binding.deleteIv.setOnClickListener(View.OnClickListener {
//            showDeleteBottomSheet()
//        })
    }

    private fun setupView(tag : String) {
        if (tag.equals("msgInbox", true)) {
            binding.activityTitleTxt.text = "Messages"
            binding.uploadIv.visibility = View.VISIBLE
            binding.forwordIv.visibility = View.VISIBLE
//            binding.deleteIv.visibility = View.GONE
            binding.infoIv.visibility = View.GONE
//            binding.buttonsLo.visibility = View.GONE
            binding.sndMessageLo.visibility = View.VISIBLE
        } else if (tag.equals("sent", true)) {
            binding.activityTitleTxt.text = "Sent Messages"
//            binding.deleteIv.visibility = View.VISIBLE
            binding.uploadIv.visibility = View.GONE
            binding.forwordIv.visibility = View.VISIBLE
            binding.infoIv.visibility = View.VISIBLE
//            binding.buttonsLo.visibility = View.GONE
            binding.sndMessageLo.visibility = View.GONE
        } else if (tag.equals("scheduled", true)) {
            binding.activityTitleTxt.text = "Scheduled Messages"
//            binding.deleteIv.visibility = View.VISIBLE
            binding.uploadIv.visibility = View.GONE
            binding.forwordIv.visibility = View.GONE
            binding.infoIv.visibility = View.GONE
//            binding.buttonsLo.visibility = View.GONE
            binding.sndMessageLo.visibility = View.GONE
        }
    }

    private fun showDeleteBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val deleteBinding = MessageDeleteBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(deleteBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(R.id.bottomSheet)
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

    private fun messagesDetails() {
        showProgress()
        var apiRequest = InboxSingleMsgReq(auth_token,inbox_message_from,message_id,scl_id,teacherId)
        Log.d("singleMsg_Req", apiRequest.toString())
        val call: Call<InboxSingleMsgResponse> = parentApiService!!.inboxSingleMsg(apiRequest)
        call.enqueue(object : Callback<InboxSingleMsgResponse> {
            override fun onResponse(call: Call<InboxSingleMsgResponse>, response: Response<InboxSingleMsgResponse>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){


                        if (loginApiResponse.response.inbox_message[0].starred_message == ""){
                            binding.startImg.setColorFilter(Color.GRAY) // Gray Tint for Read Messages
                        }else{
                            binding.startImg.setColorFilter(
                                ContextCompat.getColor(this@MessageDetailsActivity ,R.color.attendance_not_marked)
                            )
                        }

                        Glide.with(this@MessageDetailsActivity)
                            .load(Constants.IMAGES_URL+loginApiResponse.response.inbox_message[0].profile_image)
                            .placeholder(R.drawable.baseline_image)
                            .into(binding.imgs)

                        binding.subjectTv.text  = loginApiResponse.response.inbox_message[0].subject.toString()
                        binding.dateTv.text  = loginApiResponse.response.inbox_message[0].date.toString()
                        binding.fromTv.text  =   loginApiResponse.response.inbox_message[0].name.toString()
                        binding.messageTv.text  =   loginApiResponse.response.inbox_message[0].message.toString()
                        binding.usersTv.text  = loginApiResponse.response.inbox_message[0].staff_names.toString()


                        Glide.with(this@MessageDetailsActivity)
                            .load(Constants.IMAGES_URL+loginApiResponse.response.inbox_message[0].images)
                            .placeholder(R.drawable.baseline_image)
                            .into(binding.msgImages)

                        binding.startImg.setOnClickListener {
                            val message_id = loginApiResponse.response.inbox_message[0].id.toString()
                            val inbox_message_from = loginApiResponse.response.inbox_message[0].sent_from.toString()
                            val star_msg =loginApiResponse.response.inbox_message[0].starred_message.toString()
                            var inbox_message_status = ""
                            if (star_msg == ""){
                                inbox_message_status = "starred"
                            }else{
                                inbox_message_status = "unstarred"
                            }
                            callMsgUpdate(message_id,inbox_message_from,inbox_message_status)
                        }

//                        "inbox_message_status":"read,starred,archived,unstarred,unarchived",


                        binding.archivedImg.setOnClickListener {
                            val message_id = loginApiResponse.response.inbox_message[0].id.toString()
                            val inbox_message_from = loginApiResponse.response.inbox_message[0].sent_from.toString()
                            val star_msg =loginApiResponse.response.inbox_message[0].archived_message.toString()
                            var inbox_message_status = ""
                            if (star_msg == ""){
                                inbox_message_status = "archived"
                            }else{
                                inbox_message_status = "unarchived"
                            }
                            callMsgUpdate(message_id,inbox_message_from,inbox_message_status)
                        }


                        val message_id = loginApiResponse.response.inbox_message[0].id.toString()
                        val inbox_message_from = loginApiResponse.response.inbox_message[0].from.toString()
                        var inbox_message_status = "read"

                        callMsgUpdate(message_id,inbox_message_from,inbox_message_status)

                    }else{
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@MessageDetailsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<InboxSingleMsgResponse>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@MessageDetailsActivity, t.message.toString())
            }
        })
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = android.app.AlertDialog.Builder(this@MessageDetailsActivity,
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            val result: Boolean = Utility.checkPermission(this@MessageDetailsActivity)
            if (items[item] == "Take Photo") {
                // userChoosenTask = "Take Photo"
                openCamera()
            } else if (items[item] == "Choose from Gallery") {
                //userChoosenTask = "Choose from Gallery"
                openGallery()
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            this?.let {
                intent.resolveActivity(this.packageManager)?.also {
                    photoUri = createImageUri()
                    resultLauncher.launch(photoUri)
//                    resultLauncher.launch(intent)
//                    pickImageLauncher.launch(intent)
                }
            }
        }
    }

    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            this?.let {
                intent.resolveActivity(this.packageManager)?.also {
                    pickImageLauncher.launch(intent)
                }
            }
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data?.data != null) {
            val imageUri = result.data!!.data!!
            imageUris.add(imageUri)
            imageAdapter.notifyDataSetChanged()

        }
    }

    private fun convertImagesToBase64() {

        attachment_type = "image"
        val base64Strings = imageUris.mapNotNull { uri -> uriToBase64(this@MessageDetailsActivity, uri) }
        commaSeparatedBase64 = base64Strings.joinToString(",")
        Log.d("base64String", commaSeparatedBase64.toString())
    }

    private fun uriToBase64(context: MessageDetailsActivity, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Create a URI where the camera image should be saved.
    private fun createImageUri(): Uri {
        // Create a unique file name in the app's external pictures directory.
        val imageFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "camera_image_${System.currentTimeMillis()}.jpg")
        // Get URI using FileProvider.
        return FileProvider.getUriForFile(this, "${packageName}.provider", imageFile)
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

                        if (inbox_message_status == "read"){

                        }else{
                            messagesDetails()
                        }

                    }else{
                        hideProgress()
                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@MessageDetailsActivity, "Failure")
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@MessageDetailsActivity, t.message.toString())
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this@MessageDetailsActivity, HomeActivity::class.java)
        intent.putExtra("tag","msgInbox")
        startActivity(intent)
        finish()
    }


}