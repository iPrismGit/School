package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.parentapp.model.appreview.AppReviewApiResponse
import com.iprism.parentapp.model.appreview.AppReviewRequest
import com.iprism.school.repositories.AppReviewRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class AppReviewViewModel(private val repository: AppReviewRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<AppReviewApiResponse>>()
    val response: LiveData<UiState<AppReviewApiResponse>> = _response

    fun insertReview(request : AppReviewRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.insertReview(request)
                if (response.status) {
                    _response.value = UiState.Success(response)
                } else {
                    _response.value = UiState.Error(response.message)
                }
            } catch (e: Exception) {
                _response.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}