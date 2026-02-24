package com.iprism.school.activities.album

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.base.BaseActivity
import com.iprism.school.activities.ViewImageActivity
import com.iprism.school.adapters.AlbumImagesAdapter
import com.iprism.school.databinding.ActivityAlbumDetailsBinding
import com.iprism.school.interfaces.OnAlbumClickListener
import com.iprism.school.model.albums.AlbumsGallery
import com.iprism.school.repositories.AlbumsRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AlbumsViewModel
import com.iprism.school.viewModels.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AlbumDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityAlbumDetailsBinding
    private var albumId: String = ""
    private var albumName: String = ""
    private lateinit var albumsViewModel: AlbumsViewModel
    private lateinit var albumImagesAdapter: AlbumImagesAdapter
    private var albumImagesList = mutableListOf<AlbumsGallery>()
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val selectedImageUris = mutableListOf<Uri>()
    private val MAX_SELECTION = 5
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        albumId = intent.getStringExtra("albumId").toString()
        albumName = intent.getStringExtra("albumName").toString()
        binding.titleTxt.text = albumName
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                if (result.resultCode == Activity.RESULT_OK) {

                    selectedImageUris.clear()
                    val data = result.data

                    if (data?.clipData != null) {

                        val count = data.clipData!!.itemCount

                        if (count > MAX_SELECTION) {
                            Toast.makeText(
                                this,
                                "You can select maximum $MAX_SELECTION images",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@registerForActivityResult
                        }

                        for (i in 0 until count) {
                            selectedImageUris.add(data.clipData!!.getItemAt(i).uri)
                        }

                    } else if (data?.data != null) {
                        selectedImageUris.add(data.data!!)
                    }

                    updateSelectedImagesLayout()
                }
            }

        handleBack()
        initViewModel()
        setupRecyclerView()
        observeResponse()
        observeInsertAlbumsResponse()
        fetchAlbumImages()
        handleAddBtn()
        handleSendBtn()
    }

    private fun observeInsertAlbumsResponse() {
        albumsViewModel.insertImagesResponse.observe(this) { result ->

            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                    binding.backIv.isEnabled = false
                    binding.addBtn.isEnabled = false
                    binding.sendBtn.isEnabled = false
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.backIv.isEnabled = true
                    binding.addBtn.isEnabled = true
                    binding.sendBtn.isEnabled = true
                    selectedImageUris.clear()
                    updateSelectedImagesLayout()
                    ToastUtils.showSuccessCustomToast(this, "Images Added Successfully..!")
                    currentPage = 1
                    isLastPage = false
                    isLoading = false
                    albumImagesList.clear()
                    albumImagesAdapter.notifyDataSetChanged()
                    fetchAlbumImages()
                }

                is UiState.Error -> {

                    binding.progress.hideProgress()
                    binding.backIv.isEnabled = true
                    binding.addBtn.isEnabled = true
                    binding.sendBtn.isEnabled = true
                }
            }
        }
    }

    private fun handleSendBtn() {
        binding.sendBtn.setOnClickListener { view ->
            if (selectedImageUris.isEmpty()){
                ToastUtils.showErrorCustomToast(this, "Please Select Images..!")
            } else{
                val albumId = RequestBody.create("text/plain".toMediaType(), albumId)
                val type = RequestBody.create("text/plain".toMediaType(), "image")
                val page = RequestBody.create("text/plain".toMediaType(), currentPage.toString())
                val userId = RequestBody.create("text/plain".toMediaType(), userDetails[User.ID].toString())
                val viewType = RequestBody.create("text/plain".toMediaType(), "insert")
                val imageParts = prepareImageParts()
                albumsViewModel.insertAlbumMedia(
                    userId,
                    albumId,
                    viewType,
                    page,
                    type,
                    imageParts
                )
            }
        }
    }

    private fun prepareImageParts(): List<MultipartBody.Part> {

        val parts = mutableListOf<MultipartBody.Part>()

        for ((index, uri) in selectedImageUris.withIndex()) {

            val inputStream = contentResolver.openInputStream(uri)
            val bytes = inputStream!!.readBytes()

            val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())

            val part = MultipartBody.Part.createFormData(
                "media[$index]",   // <-- API key name
                "image_$index.jpg",
                requestBody
            )

            parts.add(part)
        }

        return parts
    }

    private fun updateSelectedImagesLayout() {
        if (selectedImageUris.isNotEmpty()) {
            binding.selectedImagesLayout.visibility = View.VISIBLE
            binding.selectedCountTxt.text =
                "You have selected ${selectedImageUris.size} items"
        } else {
            binding.selectedImagesLayout.visibility = View.GONE
        }
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener { view ->
            finish()
        }
    }

    private fun initViewModel() {
        val repository = AlbumsRepository(this)
        val factory = ViewModelFactory { AlbumsViewModel(repository) }
        albumsViewModel = ViewModelProvider(this, factory)[AlbumsViewModel::class.java]
    }

    fun fetchAlbumImages() {
        Log.d("AlbumImagesAPI", """ albumId = $albumId
    type = image
    page = $currentPage
    userId = ${userDetails[User.ID]}
    viewType = view
""".trimIndent())

        val albumId = RequestBody.create("text/plain".toMediaType(), albumId)
        val type = RequestBody.create("text/plain".toMediaType(), "image")
        val page = RequestBody.create("text/plain".toMediaType(), currentPage.toString())
        val userId = RequestBody.create("text/plain".toMediaType(), userDetails[User.ID].toString())
        val viewType = RequestBody.create("text/plain".toMediaType(), "view")

        albumsViewModel.uploadAlbumMedia(
            userId,
            albumId,
            viewType,
            page,
            type,
            emptyList()
        )
    }

    private fun loadMoreItems() {
        if (isLastPage || isLoading) return
        isLoading = true
        currentPage++
        albumImagesAdapter.showLoadingFooter()
        fetchAlbumImages()
    }

    private fun setupRecyclerView() {
        albumImagesAdapter = AlbumImagesAdapter(this, albumImagesList as ArrayList<AlbumsGallery?>)
        val linearLayoutManager = GridLayoutManager(this, 3)
        binding.albumImagesRv.apply {
            layoutManager = linearLayoutManager
            adapter = albumImagesAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreItems()
                        }
                    }
                }
            })
        }
            albumImagesAdapter.setupListener(object : OnAlbumClickListener{
                override fun onCoverClick(albumId: String, albumName: String) {
                    var intent = Intent(this@AlbumDetailsActivity, ViewImageActivity::class.java)
                    intent.putExtra("EventImage", albumName)
                    intent.putExtra("EventName", this@AlbumDetailsActivity.albumName)
                    startActivity(intent)
                }

            })
        }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeResponse() {
        albumsViewModel.uploadMediaResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.noDataTxt.visibility = View.GONE
                    binding.progress.hideProgress()
                    isLoading = false
                    albumImagesAdapter.removeLoadingFooter()
                    val newBookings = result.data.albums_gallery
                    if (newBookings.isNotEmpty()) {
                        albumImagesList.addAll(newBookings)
                        albumImagesAdapter.notifyDataSetChanged()
                        if (result.data.pagination.total_pages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    albumImagesAdapter.removeLoadingFooter()
                    binding.progress.hideProgress()
                    if (result.message.equals("no data found", true)) {
                        binding.noDataTxt.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun handleAddBtn() {
        binding.addBtn.setOnClickListener(View.OnClickListener {
            openGallery()
        })
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        galleryLauncher.launch(intent)
    }

}