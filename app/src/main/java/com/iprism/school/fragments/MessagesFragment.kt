package com.iprism.school.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.adapters.HelpTutorialAdapter
import com.iprism.school.adapters.MessagesAdapter
import com.iprism.school.base.BaseFragment
import com.iprism.school.databinding.FragmentHelpTutorialsBinding
import com.iprism.school.databinding.FragmentMessagesBinding
import com.iprism.school.model.helptutorials.HelpTutorial
import com.iprism.school.model.helptutorials.HelpTutorialsApiRequest
import com.iprism.school.model.messagemodel.MessageThread
import com.iprism.school.model.messagemodel.MessagesApiRequest
import com.iprism.school.repositories.HelpTutorialsRepository
import com.iprism.school.repositories.MessagesRepository
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.HelpTutorialsViewModel
import com.iprism.school.viewModels.MessagesViewModel
import com.iprism.school.viewModels.ViewModelFactory

class MessagesFragment : BaseFragment() {

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MessagesViewModel
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var messages = mutableListOf<MessageThread>()
    private lateinit var messagesAdapter: MessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setUpAdapter()
        setupObservers()
        fetchChats()
    }

    private fun initViewModel() {
        val repository = MessagesRepository(requireContext())
        viewModel = ViewModelProvider(this, ViewModelFactory {
            MessagesViewModel(repository)
        })[MessagesViewModel::class.java]
    }

    private fun fetchChats() {
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
            userDetails[User.STUDENT_ID]!!,
            "",
            userDetails[User.ID]!!,
            "view"
        )
        viewModel.fetchChats(request)
        Log.d("requestLoading", request.toString())
    }

    private fun setUpAdapter() {
        messagesAdapter = MessagesAdapter(messages as ArrayList<MessageThread?>)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvHelpTutorials.apply {
            layoutManager = linearLayoutManager
            adapter = messagesAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition =
                        linearLayoutManager.findFirstVisibleItemPosition()
                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreTutorials()
                        }
                    }
                }
            })
        }
    }

    private fun setupObservers() {
        viewModel.messagesResponse.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.noDataFoundLo.visibility = View.GONE
                    binding.progress.hideProgress()
                    isLoading = false
                    messagesAdapter.removeLoadingFooter()
                    val newBookings = state.data.response.message_threads
                    if (newBookings.isNotEmpty()) {
                        messages.addAll(newBookings)
                        messagesAdapter.notifyDataSetChanged()
                        if (state.data.response.pagination.total_pages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    messagesAdapter.removeLoadingFooter()
                    binding.progress.hideProgress()
                    if (state.message.equals("no data found", true)) {
                        binding.noDataFoundLo.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadMoreTutorials() {
        isLoading = true
        currentPage += 1
        messagesAdapter.showLoadingFooter()
        fetchChats()
    }

}