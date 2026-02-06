package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.contentpagesmodel.ContentPagesApiRequest
import com.iprism.school.model.contentpagesmodel.ContentPagesResponse
import com.iprism.school.repositories.ContentPagesRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class ContentPagesViewModel(private  val repository: ContentPagesRepository ) : ViewModel() {

    private val _contentPagesResponse = MutableLiveData<UiState<ContentPagesResponse>>()
    val contentPagesResponse: LiveData<UiState<ContentPagesResponse>> = _contentPagesResponse


    fun fetchAppContent(request : ContentPagesApiRequest) {
        viewModelScope.launch {
            _contentPagesResponse.value = UiState.Loading
            try {
                val response = repository.fetchAppContent(request)
                if (response.status) {
                    _contentPagesResponse.value = UiState.Success(response.response)
                } else {
                    _contentPagesResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _contentPagesResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}