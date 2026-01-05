package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.circularmodels.CircularApiRequest
import com.iprism.school.model.circularmodels.CircularResponse
import com.iprism.school.repositories.CircularRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class CircularViewModel(private val repository: CircularRepository) : ViewModel() {

    private val _circularsResponse = MutableLiveData<UiState<CircularResponse>>()
    val circularsResponse: LiveData<UiState<CircularResponse>> = _circularsResponse

    fun fetchCirculars(request : CircularApiRequest) {
        viewModelScope.launch {
            _circularsResponse.value = UiState.Loading
            try {
                val response = repository.getCirculars(request)
                if (response.status) {
                    _circularsResponse.value = UiState.Success(response.response)
                } else {
                    _circularsResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _circularsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}