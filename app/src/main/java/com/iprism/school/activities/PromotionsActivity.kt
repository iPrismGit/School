package com.iprism.school.activities

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.iprism.school.R
import com.iprism.school.databinding.ActivityPromotionsBinding
import com.iprism.school.utils.ToastUtils
import java.io.ByteArrayOutputStream
import java.io.InputStream

class PromotionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPromotionsBinding

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                handleSelectedMedia(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromotionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.promotionsImg.borderWidth = 4
        binding.promotionsImg.borderColor = ContextCompat.getColor(this, R.color.blue)
        handleBack()
        handleImageIv()
        handleSaveBtn()
    }

    private fun handleSaveBtn() {
        binding.saveBtn.setOnClickListener(View.OnClickListener {
            ToastUtils.showSuccessCustomToast(this, "Promotion Sent Successfully..")
            finish()
        })
    }

    private fun handleImageIv() {
        binding.promotionsImg.setOnClickListener(View.OnClickListener {
            pickMedia.launch(arrayOf("image/*", "video/*"))
        })
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }


    private fun handleSelectedMedia(uri: Uri) {
        val mimeType = contentResolver.getType(uri)
        if (mimeType?.startsWith("image") == true) {
            // If it's an image, display and convert it to Base64
            binding.promotionsImg.setImageURI(uri)
            val base64Image = convertImageToBase64(uri)
            base64Image?.let {
                Log.d("Base64Body", it)
            }
        } else if (mimeType?.startsWith("video") == true) {
            val thumbnail = getVideoThumbnail(uri)
            thumbnail?.let {
                binding.promotionsImg.setImageBitmap(it)
            }
            val base64Video = convertVideoToBase64(uri)
            base64Video?.let {
                Log.d("Base64Body", it)
            }
        }
    }

    private fun convertImageToBase64(imageUri: Uri): String? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val byteArray = outputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun convertVideoToBase64(videoUri: Uri): String? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(videoUri)
            val byteArray = inputStream?.readBytes()
            byteArray?.let {
                Base64.encodeToString(it, Base64.DEFAULT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getVideoThumbnail(videoUri: Uri): Bitmap? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(this, videoUri)
            retriever.getFrameAtTime(1000000)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            retriever.release()
        }
    }

}