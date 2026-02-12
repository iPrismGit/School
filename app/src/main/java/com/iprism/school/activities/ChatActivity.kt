package com.iprism.school.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iprism.school.R
import com.iprism.school.adapters.ChatAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityChatBinding
import com.iprism.school.databinding.FileTypeBottomSheetBinding
import com.iprism.school.model.messagemodel.MessagesApiRequest
import com.iprism.school.model.messagemodel.MessagesItem
import com.iprism.school.repositories.MessagesRepository
import com.iprism.school.utils.Constants
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.MessagesViewModel
import com.iprism.school.viewModels.ViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.apply
import kotlin.collections.filter
import kotlin.collections.isNotEmpty
import kotlin.collections.maxOf
import kotlin.jvm.java
import kotlin.text.clear
import kotlin.text.equals
import kotlin.text.get
import kotlin.text.isNotEmpty
import kotlin.text.trim

class ChatActivity : BaseActivity() {

    private lateinit var binding: ActivityChatBinding
    private var threadId = ""
    private var name =""
    private var messageType = ""
    private var image = ""
    private var latestMessageId = 0
    private lateinit var viewModel: MessagesViewModel
    private var isLoading = false
    private var isLastPage = false
    private var isFirstLoaded = true
    private var currentPage = 1
    private var messages = mutableListOf<MessagesItem>()
    private lateinit var chatAdapter: ChatAdapter
    private var pollingJob: Job? = null
    private lateinit var fileTypeBinding: FileTypeBottomSheetBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var pickFileLauncher: ActivityResultLauncher<Array<String>>
    private var selectedFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, imeInsets.bottom)
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
        threadId = intent.getStringExtra("threadId").toString()
        name = intent.getStringExtra("name").toString()
        image = intent.getStringExtra("image").toString()
        messageType = intent.getStringExtra("messageType").toString()
        setupData()
        setupListeners()
        handleBack()
        setUpCirculars()
        initViewModel()
        observeResponse()
        observeNewResponse()
        observeInsertResponse()
        fetchMessages()
        handleAttachmentBtn()
        handleCrossBtn()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeInsertResponse() {
        viewModel.insertMessageResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (selectedFileUri != null) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun setupData() {
       if (messageType.equals("single", true)){
           binding.tvName.text = name
           if (image.isNotEmpty()){
               Glide.with(this).load(Constants.IMAGES_URL + image).error(ContextCompat.getDrawable(this,
                   R.drawable.message_profile)).into(binding.ivProfile)
           }else{
               ContextCompat.getDrawable(this, R.drawable.message_profile)
           }

       }else{
           binding.tvName.text = "Group Message"
           binding.ivProfile.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.group_icon))
       }
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoaded) {
            isFirstLoaded = false
            lifecycleScope.launch {
                delay(3000)
                startPolling()
            }
        } else {
            startPolling()
        }
    }

    override fun onPause() {
        super.onPause()
        pollingJob?.cancel()
    }

    private fun startPolling() {
        if (pollingJob?.isActive == true) return
        pollingJob = lifecycleScope.launch {
            while (isActive) {
                fetchNewMessages()
                delay(3000)
            }
        }
    }

    private fun handleAttachmentBtn() {
        binding.fileImg.setOnClickListener { view ->
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
        binding.uploadedFileImg.visibility = View.VISIBLE
        binding.fileImg.visibility = View.GONE
        binding.checkInImg.setImageURI(selectedFileUri)
    }

    private fun handleCrossBtn() {
        binding.deleteImg.setOnClickListener {
            selectedFileUri = null
            binding.uploadedFileImg.visibility = View.GONE
            binding.fileImg.visibility = View.VISIBLE
        }
    }

    private fun convertUriToBase64Image(imageUri: Uri?): String {
        var base64Image = ""
        if (imageUri == null) return base64Image

        try {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            bitmap?.let {
                val byteArrayOutputStream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
                base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return base64Image
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivSend.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                insertMessage(message)
                binding.etMessage.text.clear()
                selectedFileUri = null
                binding.uploadedFileImg.visibility = View.GONE
                binding.fileImg.visibility = View.VISIBLE
            } else {
                showToast("Please Enter a Valid Message")
            }
        }
    }

    private fun handleBack() {
        binding.ivBack.setOnClickListener(View.OnClickListener {
           finish()
        })
    }

    private fun setUpCirculars() {
        val linearLayoutManager = LinearLayoutManager(this).apply {
            reverseLayout = true
        }
        chatAdapter = ChatAdapter(messages as ArrayList<MessagesItem?>)
        binding.rvChat.apply {
            layoutManager = linearLayoutManager
            adapter = chatAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    if (!isLoading && !isLastPage && lastVisibleItemPosition == messages.size - 1) {
                        loadMoreDoctors()
                    }
                }
            })
        }
    }

    private fun initViewModel() {
        val repository = MessagesRepository(this)
        val factory = ViewModelFactory { MessagesViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[MessagesViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        viewModel.response.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.noDataFoundTxt.visibility = View.GONE
                    binding.progress.hideProgress()
                    isLoading = false
                    chatAdapter.removeLoadingFooter()
                    val newBookings = result.data.response.messages
                    if (newBookings.isNotEmpty()) {
                        if (currentPage == 1 && newBookings.isNotEmpty()) {
                            latestMessageId = newBookings.maxOf { it.id }
                        }
                        messages.addAll(newBookings)
                        chatAdapter.notifyDataSetChanged()
                        if (result.data.response.pagination.total_pages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    chatAdapter.removeLoadingFooter()
                    binding.progress.hideProgress()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataFoundTxt.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeNewResponse() {
        viewModel.newResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                }

                is UiState.Success -> {
                    val newMessages = result.data.response.messages
                    if (newMessages.isNotEmpty()) {
                        val filteredMessages = newMessages.filter { it.id > latestMessageId }
                        if (filteredMessages.isNotEmpty()) {
                            latestMessageId = filteredMessages.maxOf { it.id }
                            messages.addAll(0, filteredMessages)
                            chatAdapter.notifyItemRangeInserted(0, filteredMessages.size)
                            binding.rvChat.scrollToPosition(0)
                        }
                    }
                }

                is UiState.Error -> {
                }
            }
        }
    }

    private fun fetchMessages() {
        val request = MessagesApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID]!!,
            userDetails[User.SCHOOL_ID]!!,
            "",
            "",
            "",
            "",
            currentPage,
            "",
            "teacher",
            "",
            threadId,
            "",
            "messages"
        )
        viewModel.fetchMessages(request)
        Log.d("requestLoading", request.toString())
    }

    private fun fetchNewMessages() {
        val request = MessagesApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID]!!,
            userDetails[User.SCHOOL_ID]!!,
            "",
            "",
            "",
            messageType,
            currentPage,
            "",
            "teacher",
            "",
            threadId,
            userDetails[User.ID].toString(),
            "messages"
        )
        viewModel.fetchNewMessages(request)
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreDoctors() {
        isLoading = true
        currentPage += 1
        chatAdapter.showLoadingFooter()
        fetchMessages()
    }

    fun insertMessage(message: String) {
        val request = MessagesApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID]!!,
            userDetails[User.SCHOOL_ID]!!,
            "",
            convertUriToBase64Image(selectedFileUri),
            message,
            "single",
            0,
            "",
            "teacher",
            "",
            threadId,
            userDetails[User.ID]!!,
            "insert",
        )
        viewModel.insertMessage(request)
        Log.d("requestLoading", request.toString())
    }
}