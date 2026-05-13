package com.iprism.school.activities

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
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
import com.iprism.school.databinding.ActivityDayCareChatBinding
import com.iprism.school.databinding.FileTypeBottomSheetBinding
import com.iprism.school.interfaces.OnMessageClickListener
import com.iprism.school.model.messagemodel.DayCareMessagesApiRequest
import com.iprism.school.model.messagemodel.MessagesApiRequest
import com.iprism.school.model.messagemodel.MessagesItem
import com.iprism.school.repositories.DayCareMessagesRepository
import com.iprism.school.repositories.MessagesRepository
import com.iprism.school.utils.Constants
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.DayCareMessagesViewModel
import com.iprism.school.viewModels.MessagesViewModel
import com.iprism.school.viewModels.ViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DayCareChatActivity : BaseActivity() {

    private lateinit var binding: ActivityDayCareChatBinding
    private var threadId = ""
    private var name = ""
    private var messageType = ""
    private var studentId = ""
    private var image = ""
    private var latestMessageId = 0
    private var chatStatus = ""
    private lateinit var viewModel: DayCareMessagesViewModel
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
        binding = ActivityDayCareChatBinding.inflate(layoutInflater)
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
        studentId = intent.getStringExtra("studentId").toString()
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

    private fun setupData() {
        if (messageType.equals("single", true)) {
            binding.tvName.text = name
            if (image.isNotEmpty()) {
                Glide.with(this).load(Constants.IMAGES_URL + image).error(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.message_profile
                    )
                ).into(binding.profileIv)
            } else {
                binding.profileIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.message_profile))
            }

        } else {
            binding.tvName.text = "Group Message"
            binding.profileIv.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.group_icon
                )
            )
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

        val mimeType = contentResolver.getType(uri)

        if (mimeType != null && mimeType.startsWith("image/")) {
            binding.checkInImg.setImageURI(uri)
        } else {
            binding.checkInImg.setImageResource(R.drawable.document_icon)
        }
    }

    private fun handleCrossBtn() {
        binding.deleteImg.setOnClickListener {
            selectedFileUri = null
            binding.uploadedFileImg.visibility = View.GONE
            binding.fileImg.visibility = View.VISIBLE
        }
    }

    private fun convertUriToBase64Image(uri: Uri?): String {
        if (uri == null) return ""

        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            if (bytes != null) {
                Base64.encodeToString(bytes, Base64.NO_WRAP)
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
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
                        loadMoreMessages()
                    }
                }
            })
        }
        chatAdapter.setupListener(object : OnMessageClickListener {
            override fun onItemClick(
                threadId: String,
                name: String,
                image: String,
                type: String,
                studentId: String,
                studentType: String
            ) {

            }

            override fun onStudentSelectClick(
                value: String,
                studentId: String,
                studentName: String
            ) {

            }

            override fun onInnerItemClick(eventImage: String) {
                if (eventImage.isNotEmpty()) {
                    if (eventImage.endsWith(".pdf")) {
                        var intent = Intent(this@DayCareChatActivity, PdfViewActivity::class.java)
                        intent.putExtra("pdfUrl", eventImage)
                        startActivity(intent)
                    } else {
                        var intent = Intent(this@DayCareChatActivity, ViewImageActivity::class.java)
                        intent.putExtra("EventImage", eventImage)
                        intent.putExtra("EventName", "Message Image")
                        startActivity(intent)
                    }
                }
            }

        })
    }

    private fun initViewModel() {
        val repository = DayCareMessagesRepository(this)
        val factory = ViewModelFactory { DayCareMessagesViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[DayCareMessagesViewModel::class.java]
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
                    chatStatus = result.data.response.chat_status
                    if (chatStatus.equals("1",true)){
                        binding.replyLo.visibility = View.VISIBLE
                        binding.restrictTxt.visibility = View.GONE
                    } else{
                        binding.replyLo.visibility = View.GONE
                        binding.restrictTxt.visibility = View.VISIBLE
                    }
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
                    if (chatStatus.equals("1",true)){
                        binding.replyLo.visibility = View.VISIBLE
                        binding.restrictTxt.visibility = View.GONE
                    } else{
                        binding.replyLo.visibility = View.GONE
                        binding.restrictTxt.visibility = View.VISIBLE
                    }
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
                    fetchNewMessages()
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun fetchMessages() {
        val request = DayCareMessagesApiRequest(
            userDetails[User.SCHOOL_ID]!!,
            "0",
            "",
            "",
            "",
            currentPage.toString(),
            "teacher",
            studentId,
            threadId,
            userDetails[User.ID]!!,
            "messages"
        )
        viewModel.fetchMessages(request)
        Log.d("requestLoading", request.toString())
    }

    private fun fetchNewMessages() {
        val request = DayCareMessagesApiRequest(
            userDetails[User.SCHOOL_ID]!!,
            "",
            "",
            "",
            "",
            currentPage.toString(),
            "teacher",
            studentId,
            threadId,
            userDetails[User.ID]!!,
            "messages"
        )
        viewModel.fetchNewMessages(request)
        Log.d("requestLoading", request.toString())
    }

    private fun loadMoreMessages() {
        isLoading = true
        currentPage += 1
        chatAdapter.showLoadingFooter()
        fetchMessages()
    }

    fun insertMessage(message: String) {
        val request = DayCareMessagesApiRequest(
            userDetails[User.SCHOOL_ID]!!,
            "0",
            convertUriToBase64Image(selectedFileUri),
            message,
            "single",
            currentPage.toString(),
            "teacher",
            studentId,
            threadId,
            userDetails[User.ID]!!,
            "insert"
        )
        viewModel.insertMessage(request)
        Log.d("requestLoading1", request.toString())
        Log.d("imageBas64", convertUriToBase64Image(selectedFileUri))
    }

}