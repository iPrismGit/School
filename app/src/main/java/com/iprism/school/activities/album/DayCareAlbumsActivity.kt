package com.iprism.school.activities.album

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.iprism.school.R
import com.iprism.school.activities.DayCarePlansActivity
import com.iprism.school.adapters.AlbumCoversAdapter
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityDayCareAlbumsBinding
import com.iprism.school.databinding.ViewMessagesAlertDialogBinding
import com.iprism.school.interfaces.OnAlbumClickListener
import com.iprism.school.model.albums.AlbumCover
import com.iprism.school.model.albums.AlbumCoverImagesApiRequest
import com.iprism.school.model.albums.DayCareAlbumsApiRequest
import com.iprism.school.model.daycare.DayCareStatusApiRequest
import com.iprism.school.repositories.AlbumsRepository
import com.iprism.school.repositories.DayCareRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.AlbumsViewModel
import com.iprism.school.viewModels.DayCareViewModel
import com.iprism.school.viewModels.ViewModelFactory

class DayCareAlbumsActivity : BaseActivity() {

    private lateinit var binding: ActivityDayCareAlbumsBinding
    private lateinit var albumsViewModel: AlbumsViewModel
    private lateinit var albumCoversAdapter: AlbumCoversAdapter
    private lateinit var dayCareViewModel: DayCareViewModel
    private var albumCoversList = mutableListOf<AlbumCover>()
    private var isFreshLoad = false
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDayCareAlbumsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleAddBtn()
        handleBack()
        initViewModel()
        setupRecyclerView()
        observeAlbumsResponse()
        loadAlbumCovers()
        handleRefreshLo()
        observeDayCareStatusResponse()
    }

    private fun observeDayCareStatusResponse() {
        dayCareViewModel.dayCareStatusResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    if (result.data.status.equals("yes", true)) {
                        var intent = Intent(this, CreateDayCareAlbumsActivity::class.java)
                        intent.putExtra("tag", "DayCare")
                        startActivity(intent)
                    } else {
                        showConfirmationDialog()
                    }
                }

                is UiState.Error -> {
                    ToastUtils.showErrorCustomToast(this, result.message)
                    Log.d("Message", result.message)
                    binding.progress.hideProgress()
                }
            }
        }
    }

    private fun showConfirmationDialog() {
        val binding = ViewMessagesAlertDialogBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(this)
            .setView(binding.root)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.show()

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        binding.okBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun initViewModel() {
        val repository = AlbumsRepository(this)
        val factory = ViewModelFactory { AlbumsViewModel(repository) }
        albumsViewModel = ViewModelProvider(this, factory)[AlbumsViewModel::class.java]

        val dayCareRepository = DayCareRepository(this)
        val dayCareFactory = ViewModelFactory { DayCareViewModel(dayCareRepository) }
        dayCareViewModel = ViewModelProvider(this, dayCareFactory)[DayCareViewModel::class.java]
    }

    private fun loadAlbumCovers(isFromFilterChange: Boolean = false) {

        if (isLoading) return

        if (isFromFilterChange) {
            currentPage = 1
            isLastPage = false
            isFreshLoad = true

            albumCoversList.clear()
            albumCoversAdapter.notifyDataSetChanged()

            binding.albumCoversRv.visibility = View.GONE
            binding.noDataTxt.visibility = View.VISIBLE
        }

        isLoading = true

        val request = DayCareAlbumsApiRequest(
            userDetails[User.ACADEMIC_YEAR_ID].toString(),
            userDetails[User.SCHOOL_ID].toString(),
            "",
            "",
            "",
            "",
            currentPage, "", userDetails[User.ID].toString(), "view"
        )

        Log.d("AlbumCoversApiRequest", request.toString())
        albumsViewModel.fetchDayCareAlbumCovers(request)
    }

    private fun refreshItems() {
        currentPage = 1
        isLastPage = false
        isLoading = false
        albumCoversList.clear()
        albumCoversAdapter.notifyDataSetChanged()
        loadAlbumCovers(isFromFilterChange = true)
    }

    private fun loadMoreItems() {
        if (isLastPage || isLoading) return
        isLoading = true
        currentPage++
        loadAlbumCovers()
    }

    private fun resetEvents() {
        currentPage = 1
        isLastPage = false
        isLoading = false
        albumCoversList.clear()
        albumCoversAdapter.notifyDataSetChanged()
        binding.albumCoversRv.visibility = View.GONE
        binding.noDataTxt.visibility = View.VISIBLE
    }

    private fun handleRefreshLo() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                refreshItems()
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun setupRecyclerView() {
        albumCoversAdapter = AlbumCoversAdapter(this, albumCoversList)
        val linearLayoutManager = LinearLayoutManager(this)

        binding.albumCoversRv.apply {
            layoutManager = linearLayoutManager
            adapter = albumCoversAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    binding.refreshLayout.isEnabled =
                        !binding.albumCoversRv.canScrollVertically(-1)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage && albumCoversList.isNotEmpty()) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            loadMoreItems()
                        }
                    }

                }
            })

            albumCoversAdapter.setupListener(object  : OnAlbumClickListener{
                override fun onCoverClick(albumId: String, albumName: String) {
                    var intent = Intent(this@DayCareAlbumsActivity, DayCareAlbumDetailsActivity::class.java)
                    intent.putExtra("albumId", albumId)
                    intent.putExtra("albumName", albumName)
                    startActivity(intent)
                }

            })
        }

    }

    private fun observeAlbumsResponse() {
        albumsViewModel.dayCareAlbumCoversResponse.observe(this) { result ->

            when (result) {
                is UiState.Loading -> {
                    if (currentPage == 1) {
                        binding.progress.showProgress()
                    }
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    isLoading = false

                    val newAlbumCovers = result.data.album_covers

                    if (newAlbumCovers.isNotEmpty()) {

                        if (isFreshLoad) {
                            albumCoversList.clear()
                            isFreshLoad = false
                        }

                        albumCoversList.addAll(newAlbumCovers)
                        albumCoversAdapter.notifyDataSetChanged()

                        binding.albumCoversRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE

                        isLastPage = newAlbumCovers.size < limit
                    } else {
                        isLastPage = true

                        if (currentPage == 1) {
                            albumCoversList.clear()
                            albumCoversAdapter.notifyDataSetChanged()
                            binding.albumCoversRv.visibility = View.GONE
                            binding.noDataTxt.visibility = View.VISIBLE
                        }
                    }

                }

                is UiState.Error -> {
                    isLoading = false
                    binding.progress.hideProgress()
                    if (albumCoversList.isEmpty()) {
                        binding.albumCoversRv.visibility = View.GONE
                        binding.noDataTxt.visibility = View.VISIBLE
                        ToastUtils.showErrorCustomToast(this, result.message)
                    } else {
                        binding.albumCoversRv.visibility = View.VISIBLE
                        binding.noDataTxt.visibility = View.GONE
                        ToastUtils.showErrorCustomToast(this, "There is no more data")
                    }
                }
            }
        }
    }

    private fun handleAddBtn() {
        binding.addBtn.setOnClickListener(View.OnClickListener {
            var request = DayCareStatusApiRequest(
                userDetails[User.ACADEMIC_YEAR_ID].toString(),
                userDetails[User.SCHOOL_ID].toString(), userDetails[User.ID].toString()
            )
            dayCareViewModel.fetchDayCareStatus(request)
        })
    }

}