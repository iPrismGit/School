package com.iprism.school.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.school.R
import com.iprism.school.adapters.HelpTutorialAdapter
import com.iprism.school.adapters.HelpTutorialsAdapter
import com.iprism.school.base.BaseFragment
import com.iprism.school.databinding.FragmentHelpTutorialsBinding
import com.iprism.school.model.helptutorials.HelpTutorial
import com.iprism.school.model.helptutorials.HelpTutorialsApiRequest
import com.iprism.school.repositories.HelpTutorialsRepository
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.HelpTutorialsViewModel
import com.iprism.school.viewModels.ViewModelFactory


class HelpTutorialsFragment : BaseFragment() {

    private var _binding: FragmentHelpTutorialsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HelpTutorialsViewModel
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var helpTutorials = mutableListOf<HelpTutorial>()
    private lateinit var helpTutorialsAdapter: HelpTutorialAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHelpTutorialsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setUpAdapter()
        setupObservers()
        fetchTutorials()
    }

    private fun initViewModel() {
        val repository = HelpTutorialsRepository(requireContext())
        viewModel = ViewModelProvider(this, ViewModelFactory {
            HelpTutorialsViewModel(repository)
        })[HelpTutorialsViewModel::class.java]
    }

    private fun fetchTutorials() {
        val request = HelpTutorialsApiRequest(
            userDetails[User.SCHOOL_ID]!!,
            currentPage,
            userDetails[User.ID]!!
        )
        viewModel.fetchHelpTutorials(request)
        Log.d("requestLoading", request.toString())
    }

    private fun setUpAdapter() {
        helpTutorialsAdapter = HelpTutorialAdapter(helpTutorials as ArrayList<HelpTutorial?>)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvHelpTutorials.apply {
            layoutManager = linearLayoutManager
            adapter = helpTutorialsAdapter
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
        viewModel.response.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.noDataFoundLo.visibility = View.GONE
                    binding.progress.hideProgress()
                    isLoading = false
                    helpTutorialsAdapter.removeLoadingFooter()
                    val newBookings = state.data.response.help_tutorials
                    if (newBookings.isNotEmpty()) {
                        helpTutorials.addAll(newBookings)
                        helpTutorialsAdapter.notifyDataSetChanged()
                        if (state.data.response.pagination.total_pages.size == currentPage) {
                            isLastPage = true
                        }
                    }
                }

                is UiState.Error -> {
                    isLoading = false
                    helpTutorialsAdapter.removeLoadingFooter()
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
        helpTutorialsAdapter.showLoadingFooter()
        fetchTutorials()
    }

}