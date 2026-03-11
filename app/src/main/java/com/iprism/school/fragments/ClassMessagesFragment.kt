package com.iprism.school.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iprism.school.R
import com.iprism.school.activities.ChatActivity
import com.iprism.school.activities.InitiateMessageActivity
import com.iprism.school.adapters.MessagesAdapter
import com.iprism.school.base.BaseFragment
import com.iprism.school.databinding.FragmentClassMessagesBinding
import com.iprism.school.interfaces.OnMessageClickListener
import com.iprism.school.model.messagemodel.MessageThread
import com.iprism.school.model.messagemodel.MessagesApiRequest
import com.iprism.school.repositories.MessagesRepository
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.MessagesViewModel
import com.iprism.school.viewModels.ViewModelFactory

class ClassMessagesFragment : BaseFragment() {

    private var _binding: FragmentClassMessagesBinding? = null
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
        _binding = FragmentClassMessagesBinding.inflate(inflater, container, false)
        binding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.blue1)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setUpAdapter()
        setupObservers()
        refresh()
        insertMessageBtn()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refresh() {
        binding.refreshLayout.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener {
                currentPage = 1
                isLastPage = false
                isLoading = false
                messages.clear()
                messagesAdapter.notifyDataSetChanged()
                fetchChats()
                binding.refreshLayout.isRefreshing = false
            }
        )
    }

    private fun insertMessageBtn() {
        binding.messageBtn.setOnClickListener { view ->
            startActivity(Intent(requireContext(), InitiateMessageActivity::class.java))
        }
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
            currentPage.toString(),
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

    override fun onResume() {
        super.onResume()
        currentPage = 1
        isLastPage = false
        isLoading = false
        messages.clear()
        messagesAdapter.notifyDataSetChanged()
        fetchChats()
    }

    private fun setUpAdapter() {
        messagesAdapter = MessagesAdapter(messages as ArrayList<MessageThread?>)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.messagesRv.apply {
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
            messagesAdapter.setupListener(object : OnMessageClickListener {
                override fun onItemClick(
                    threadId: String,
                    name: String,
                    image: String,
                    type: String,
                    studentId: String,
                    studentType: String
                ) {
                    Log.d("MessageDetails", threadId + ", " + name + ", " + image + ", " + type + ", " + studentId)
                    var intent = Intent(requireContext(), ChatActivity::class.java)
                    intent.putExtra("threadId", threadId)
                    intent.putExtra("name", name)
                    intent.putExtra("image", image)
                    intent.putExtra("messageType", type)
                    intent.putExtra("studentId", studentId)
                    startActivity(intent)

                }

                override fun onStudentSelectClick(
                    value: String,
                    studentId: String,
                    studentName: String
                ) {

                }

                override fun onInnerItemClick(eventImage: String) {

                }

            })
        }
    }

    private fun setupObservers() {
        viewModel.messagesResponse.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    if (currentPage == 1){
                        binding.progress.showProgress()
                    }
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