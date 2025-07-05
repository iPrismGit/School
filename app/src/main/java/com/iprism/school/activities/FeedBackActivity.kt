package com.iprism.school.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.iprism.parentapp.base.BaseActivity
import com.iprism.school.R
import com.iprism.school.activities.PromotionsActivity
import com.iprism.school.databinding.ActivityFeedBackBinding
import com.iprism.school.model.Request.CreatePromotionsReq
import com.iprism.school.model.Request.SendFeedBackReq
import com.iprism.school.model.Response.SuccessResponsePojo
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.User
import com.iprism.school.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class FeedBackActivity : BaseActivity() {

    private lateinit var binding: ActivityFeedBackBinding
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var resultLaunchergallery: ActivityResultLauncher<Intent>
    private var encodedPic: String? = ""
    private var feedBAckImageUri: Uri? = null
    private var selectedValure: String? = null
    private var ratingValue: String? = null
    private var bitmap: Bitmap? = null

    private var tag: String = ""
    private var teacherId: String = ""
    private var auth_token: String = ""
    private var scl_id: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBackBinding.inflate(layoutInflater)
        setContentView(binding.root)


        teacherId = userDetails[User.Companion.ID].toString()
        auth_token = userDetails[User.Companion.AUTH_TOKEN].toString()
        scl_id = userDetails[User.Companion.SCHOOL_ID].toString()

        handleSelectedImageBtn()
        handleSendBtn()
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
//            Toast.makeText(this, "Rating: $rating", Toast.LENGTH_SHORT).show()
            Log.d("RatingValue", rating.toString())
            ratingValue = rating.toString()

        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val bitmap = data?.extras?.get("data") as Bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val b = baos.toByteArray()
                val encoder: java.util.Base64.Encoder = java.util.Base64.getEncoder()
                binding.selectedImg.setImageBitmap(bitmap)
                encodedPic = encoder.encodeToString(b)
            }
        }

        resultLaunchergallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val uri = data?.data
                val imagestrem : InputStream? = contentResolver?.openInputStream(uri!!)
                val selectedImage  : Bitmap = BitmapFactory.decodeStream(imagestrem)
                binding.selectedImg.setImageBitmap(selectedImage)
                encodedPic = encodeImage(selectedImage)
            }
        }


        binding.btnGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            val selectedText = selectedRadioButton.text.toString()

            selectedValure = selectedText.toString()

            // Display or use the selected value
            Toast.makeText(this, "Selected: $selectedText", Toast.LENGTH_SHORT).show()
            Log.d("SelectedOption", selectedText)
        }

    }

    private fun handleSendBtn() {
        binding.sendFeedBackIv.setOnClickListener(View.OnClickListener {
            if (selectedValure.isNullOrEmpty()) {
                showToast("Please select Suggestion")
            }else if (ratingValue.isNullOrEmpty()) {
                showToast("Please select Rating")
            }else if (binding.feedbackTxt.text.toString()== ""||binding.feedbackTxt.text.toString()== null){
                showToast("Enter Feedback")
            }else {
                sendFeedBack()
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleSelectedImageBtn() {
        binding.selectImageIv.setOnClickListener(View.OnClickListener {
            selectImage()
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun encodeImage(selectedImage: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        val b = baos.toByteArray()
        val encoder: java.util.Base64.Encoder = java.util.Base64.getEncoder()
        encodedPic = encoder.encodeToString(b)

        return encodedPic
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun selectImage() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = android.app.AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            val result: Boolean = Utility.checkPermission(this)
            if (items[item] == "Take Photo") {
                openCamera()
            } else if (items[item] == "Choose from Gallery") {
                openGalleryy()
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openGalleryy() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            resultLaunchergallery.launch(intent)
        }
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            this.let {
                intent.resolveActivity(it.packageManager)?.also {
                    resultLauncher.launch(intent)
                }
            }
        }
    }

    private fun sendFeedBack() {
        showProgress()
        var apiRequest = SendFeedBackReq(encodedPic.toString(),auth_token,ratingValue.toString(),binding.feedbackTxt.text.toString()
            ,scl_id,selectedValure.toString(),teacherId)
        Log.d("sent_feedbackReq", apiRequest.toString())
        val call: Call<SuccessResponsePojo> = parentApiService!!.sentSuggetions(apiRequest)
        call.enqueue(object : Callback<SuccessResponsePojo> {
            override fun onResponse(call: Call<SuccessResponsePojo>, response: Response<SuccessResponsePojo>) {
                if (response.isSuccessful) {
                    hideProgress()
                    val loginApiResponse = response.body()
                    if (loginApiResponse!!.status == true){
                        val intent = Intent(this@FeedBackActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{

                    }
                } else {
                    hideProgress()
                    ToastUtils.showErrorCustomToast(this@FeedBackActivity, response.message())
                }
            }
            override fun onFailure(call: Call<SuccessResponsePojo>, t: Throwable) {
                hideProgress()
                ToastUtils.showErrorCustomToast(this@FeedBackActivity, t.message.toString())
            }
        })
    }


}