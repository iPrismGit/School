package com.iprism.school.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityFeedBackBinding
import com.iprism.school.utils.ToastUtils
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException

class FeedBackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBackBinding
    private lateinit var launchSomeActivity: ActivityResultLauncher<Intent>
    private var feedBAckImageUri: Uri? = null
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createLaunchSomeActivity()
        handleSelectedImageBtn()
        handleSendBtn()
        handleCross()
    }

    private fun handleCross() {
        binding.crossIv.setOnClickListener(View.OnClickListener {
            feedBAckImageUri = null
            binding.selectedImg.visibility = View.GONE
            binding.crossIv.visibility = View.GONE
            Log.d("FeedBackNull", feedBAckImageUri.toString())
        })
    }

    private fun handleSendBtn() {
        binding.sendFeedBackIv.setOnClickListener(View.OnClickListener {
            feedBAckImageUri = null
            binding.selectedImg.visibility = View.GONE
            binding.crossIv.visibility = View.GONE
            binding.feedbackTxt.setText("")
            ToastUtils.showSuccessCustomToast(this, "Feedback Sent Successfully...")
        })
    }

    private fun handleSelectedImageBtn() {
        binding.selectImageIv.setOnClickListener(View.OnClickListener {
            selectImage()
        })
    }

    private fun selectImage() {
        val options = arrayOf("Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this@FeedBackActivity)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            when (options[item]) {
                "Choose from Gallery" -> {
                    val pickPhotoIntent = Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    ).apply {
                        type = "image/*"
                    }
                    launchSomeActivity.launch(pickPhotoIntent)
                }

                "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun createLaunchSomeActivity() {
        launchSomeActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val data: Intent? = result.data
                    feedBAckImageUri = data?.data
                   Log.d("feedBAckImageUri", feedBAckImageUri.toString())
                    feedBAckImageUri?.let {
                        binding.crossIv.visibility = View.VISIBLE
                        binding.selectedImg.visibility = View.VISIBLE
                        binding.selectedImg.setImageURI(it)
                        try {
                            val imageStream = contentResolver.openInputStream(it)
                            val selectedImage = BitmapFactory.decodeStream(imageStream)
                            // Handle the selected image (encodedImage can be processed later if needed)
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
    }

    private fun convertUriToBase64Image(imageUri: Uri?): String {
        var base64Image = ""
        if (imageUri == null) {
            return base64Image
        }

        try {
            val inputStream = contentResolver.openInputStream(imageUri)
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        bitmap?.let {
            val byteArrayOutputStream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val imageBytes = byteArrayOutputStream.toByteArray()
            base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        }

        return base64Image
    }

}