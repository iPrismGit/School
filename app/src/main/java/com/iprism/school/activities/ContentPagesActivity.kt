package com.iprism.school.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.iprism.school.base.BaseActivity
import com.iprism.school.databinding.ActivityContentPagesBinding
import com.iprism.school.model.contentpagesmodel.ContentPagesApiRequest
import com.iprism.school.repositories.ContentPagesRepository
import com.iprism.school.utils.ToastUtils
import com.iprism.school.utils.UiState
import com.iprism.school.utils.User
import com.iprism.school.utils.hideProgress
import com.iprism.school.utils.showProgress
import com.iprism.school.viewModels.ContentPagesViewModel
import com.iprism.school.viewModels.ViewModelFactory

class ContentPagesActivity : BaseActivity() {

    private lateinit var binding: ActivityContentPagesBinding
    private var tag: String = ""
    private var name: String = ""
    private lateinit var viewModel: ContentPagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentPagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        tag = intent.getStringExtra("tag").toString()
        name = intent.getStringExtra("name").toString()
        binding.textView10.text = name
        handleBack()
        initViewModel()
        observeAppContentResponse()
        var contentPagesApiRequest = ContentPagesApiRequest(userDetails[User.ID].toString(), tag)
        viewModel.fetchAppContent(contentPagesApiRequest)
    }

    private fun observeAppContentResponse() {
        viewModel.contentPagesResponse.observe(this) { result ->
            when (result) {
                is UiState.Loading -> {
                    binding.progress.showProgress()
                }

                is UiState.Success -> {
                    binding.progress.hideProgress()
                    binding.contentTxt.text = result.data.name
                }

                is UiState.Error -> {
                    binding.progress.hideProgress()
                    ToastUtils.showErrorCustomToast(this, result.message)
                }
            }
        }
    }

    private fun initViewModel() {
        val repository = ContentPagesRepository(this)
        val factory = ViewModelFactory { ContentPagesViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[ContentPagesViewModel::class.java]
    }

    private fun handleBack() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })
    }


}