package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.fees.FeesApiResponse
import com.iprism.school.model.fees.FeesRequest
import com.iprism.school.repositories.FeesRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class FeesViewModel(private val repository: FeesRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<FeesApiResponse>>()
    val response: LiveData<UiState<FeesApiResponse>> = _response

    fun fetchFees(request : FeesRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchFees(request)
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