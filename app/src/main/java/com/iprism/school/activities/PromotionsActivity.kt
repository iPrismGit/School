package com.iprism.school.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.school.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.adapters.ImageAdapter
import com.iprism.school.databinding.ActivityPromotionsBinding
import com.iprism.school.model.Request.CreatePromotionsReq
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import com.iprism.school.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream

class PromotionsActivity : BaseActivity() {

    private lateinit var binding: ActivityPromotionsBinding

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""
    private var selectedVideoUri: Uri? = null
    private var attachmentType: String? = ""

    lateinit var resultLauncher: ActivityResultLauncher<Uri>
    lateinit var resultLaunchergallery: ActivityResultLauncher<Intent>

    private val MAX_VIDEO_SIZE_MB = 30 // Max size limit

//    private val pickMedia = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
//            uri?.let {
//                handleSelectedMedia(it)
//            }
//        }


    private var commaSeparatedBase64 : String? = null
    private lateinit var imageAdapter: ImageAdapter
    private val imageUris = mutableListOf<Uri>()
    private lateinit var photoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromotionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        binding.promotionsImg.borderWidth = 4
        binding.promotionsImg.borderColor = ContextCompat.getColor(this, R.color.blue)
        handleBack()
        handleImageIv()
        handleSaveBtn()

        resultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                Log.d("cameraCapture", "Photo captured at: $photoUri")
                imageUris.add(photoUri)
                imageAdapter.notifyDataSetChanged()
            } else {
                Log.e("cameraCapture", "Failed to capture photo")
            }
        }

        binding.imagesRv.layoutManager = GridLayoutManager(this,3)
        imageAdapter = ImageAdapter(imageUris) { uri ->
            imageAdapter.deleteImage(uri) }
        binding.imagesRv.adapter = imageAdapter
        imageAdapter.notifyDataSetChanged()

        Log.d("images_Uris",imageUris.toString())

    }

    private fun handleSaveBtn() {
        binding.saveBtn.setOnClickListener(View.OnClickListener {
            if (attachmentType == "image"){
                convertImagesToBase64()
            }else{

            }

            if (commaSeparatedBase64 == ""||commaSeparatedBase64 == null){
                showToast("Select At least one Image or Video")
            }else if (binding.descriptionEt.text.toString() == ""||binding.descriptionEt.text.toString() == ""){
                showToast("Enter Description")
            }else{
                createPromotions()
            }


        })
    }

    private fun handleImageIv() {
        binding.promotionsImg.setOnClickListener(View.OnClickListener {
            selectFile()
//            pickMedia.launch(arrayOf("image/*", "video/*"))
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun selectFile() {
        val items = arrayOf<CharSequence>("Choose Image", "Choose Video", "Cancel")
        val builder = android.app.AlertDialog.Builder(this@PromotionsActivity,
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            val result: Boolean = Utility.checkPermission(this@PromotionsActivity)
            if (items[item] == "Choose Video") {
                openVideo()
                binding.videoView.visibility = View.VISIBLE
                binding.imagesRv.visibility = View.GONE
            } else if (items[item] == "Choose Image") {
                openGallery()
                binding.videoView.visibility = View.GONE
                binding.imagesRv.visibility = View.VISIBLE
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun openVideo() {
        pickVideo.launch("video/*")
        attachmentType = "video"
    }

    private fun openGallery() {
        if (imageUris.size < 3){
            Intent(Intent.ACTION_GET_CONTENT).also { intent ->
                intent.type = "image/*"
                this?.let {
                    intent.resolveActivity(this.packageManager)?.also {
                        pickImageLauncher.launch(intent)
                        attachmentType = "image"
                    }
                }
            }
        }else{
            showToast("Choose max 3 images only")
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
        val base64Strings = imageUris.mapNotNull { uri -> uriToBase64(this@PromotionsActivity, uri) }
        commaSeparatedBase64 = base64Strings.joinToString(",")
        Log.d("base64String", commaSeparatedBase64.toString())
    }

    private fun uriToBase64(context: PromotionsActivity, uri: Uri): String? {
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

    private fun createPromotions() {
        showProgress()
        var apiRequest = CreatePromotionsReq(attachmentType.toString(),commaSeparatedBase64.toString(),
            auth_token,binding.descriptionEt.text.toString(),scl_id,teacherId)
        Log.d("creatPromotionReq", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.createPromotions(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){
                        val intent = Intent(this@PromotionsActivity, PromotionsActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@PromotionsActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@PromotionsActivity, t.message.toString())
            }
        })
    }

    private val pickVideo = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val fileSizeMB = getFileSizeInMB(it)
            if (fileSizeMB > MAX_VIDEO_SIZE_MB) {
                Toast.makeText(this@PromotionsActivity, "Video size must be ≤ 30MB", Toast.LENGTH_LONG).show()
            } else {
                selectedVideoUri = it
               binding.videoView.setVideoURI(it)
                binding.videoView.start()

                // Convert to Base64
                commaSeparatedBase64 = convertVideoToBase64(it).toString()
                commaSeparatedBase64?.let { encoded ->
                    Log.d("Base64Video", encoded)
                }
            }
        }
    }

    private fun getFileSizeInMB(uri: Uri): Double {
        var fileSize: Double = 0.0
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (sizeIndex != -1) {
                cursor.moveToFirst()
                val sizeInBytes = cursor.getLong(sizeIndex)
                fileSize = sizeInBytes / (1024.0 * 1024.0) // Convert to MB
            }
        }
        return fileSize
    }

    private fun convertVideoToBase64(videoUri: Uri): String? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(videoUri)
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int

            while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }

            inputStream?.close()
            val videoBytes = byteArrayOutputStream.toByteArray()
            Base64.encodeToString(videoBytes, Base64.DEFAULT)

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}