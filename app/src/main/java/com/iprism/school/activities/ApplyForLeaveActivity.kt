package com.iprism.school.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.databinding.ActivityApplyForLeaveBinding
import com.iprism.school.databinding.FileTypeBottomSheetBinding
import com.iprism.school.utils.DateTimeUtils
import com.iprism.school.utils.ToastUtils
import java.text.SimpleDateFormat
import java.util.Locale

class ApplyForLeaveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApplyForLeaveBinding
    private lateinit var fileTypeBinding: FileTypeBottomSheetBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var pickFileLauncher: ActivityResultLauncher<Array<String>>
    private var selectedFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplyForLeaveBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                uri?.let {
                    handleSelectedFile(it)
                }
            }

        pickFileLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                uri?.let {
                    contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    handleSelectedFile(it)
                }
            }

        handleAttachmentBtn()
        handleBack()
        handleCrossBtn()
        handleStartDateLo()
        handleEndDateLo()
        handleSubmitBtn()
    }

    private fun handleSubmitBtn() {
        binding.submitBtn.setOnClickListener { view ->
            if (getStartDate().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Select Start Date..!")
            } else if (getEndDate().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Select End Date..!")
            } else if (getName().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Applicant Name..!")
            } else if (getReason().isEmpty()) {
                ToastUtils.showErrorCustomToast(this, "Please Enter Reason For Leave..!")
            } else {
                Log.d("StartAndEndDate", "Start Date: ${convertDateFormatSafe(binding.startDateTxt)}, End Date: ${convertDateFormatSafe(binding.endDateTxt)} ")
            }
        }
    }

    private fun getStartDate(): String {
        return binding.startDateTxt.text.toString().trim()
    }

    private fun getEndDate(): String {
        return binding.endDateTxt.text.toString().trim()
    }

    private fun getName(): String {
        return binding.nameTxt.text.toString().trim()
    }

    private fun getReason(): String {
        return binding.reasonTxt.text.toString().trim()
    }

    private fun handleEndDateLo() {
        binding.endDateLo.setOnClickListener { view ->
            DateTimeUtils.getDate(binding.endDateTxt, false)
        }
    }

    private fun handleStartDateLo() {
        binding.startDateLo.setOnClickListener { view ->
            DateTimeUtils.getDate(binding.startDateTxt, false)
        }
    }

    private fun convertDateFormatSafe(textView: TextView): String? {
        return try {
            val inputDate = textView.text.toString()
            val inputFormat = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val date = inputFormat.parse(inputDate)
            date?.let { outputFormat.format(it) }
        } catch (e: Exception) {
            null
        }
    }

    private fun handleCrossBtn() {
        binding.removeIv.setOnClickListener {
            selectedFileUri = null
            binding.documentLo.visibility = View.GONE
            binding.btnAttachment.visibility = View.VISIBLE
            binding.fileNameTxt.text = ""
        }

    }

    private fun handleBack() {
        binding.ivBack.setOnClickListener { view ->
            finish()
        }
    }

    private fun handleAttachmentBtn() {
        binding.btnAttachment.setOnClickListener { view ->
            openFileSelectingOptions()
        }
    }

    private fun openFileSelectingOptions() {
        bottomSheetDialog = BottomSheetDialog(this)
        fileTypeBinding = FileTypeBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(fileTypeBinding.root)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        fileTypeBinding.crossImg.setOnClickListener { view ->
            bottomSheetDialog.dismiss()
        }

        fileTypeBinding.fileLo.setOnClickListener { view ->
            bottomSheetDialog.dismiss()
            pickFileLauncher.launch(arrayOf("*/*"))
        }

        fileTypeBinding.galleryLo.setOnClickListener { view ->
            bottomSheetDialog.dismiss()
            pickImageLauncher.launch("image/*")
        }

        bottomSheetDialog.show()
    }

    private fun handleSelectedFile(uri: Uri) {
        selectedFileUri = uri
        val fileName = getFileNameFromUri(uri)
        binding.fileNameTxt.text = fileName
        binding.documentLo.visibility = View.VISIBLE
        binding.btnAttachment.visibility = View.GONE
    }

    private fun getFileNameFromUri(uri: Uri): String {
        var name = "Unknown File"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    name = it.getString(index)
                }
            }
        }
        return name
    }

}