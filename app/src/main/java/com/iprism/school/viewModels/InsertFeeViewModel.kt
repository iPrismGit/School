package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.insertfee.InsertFeeApiResponse
import com.iprism.school.model.insertfee.InsertFeeRequest
import com.iprism.school.repositories.FeesRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class InsertFeeViewModel(private val repository: FeesRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<InsertFeeApiResponse>>()
    val response: LiveData<UiState<InsertFeeApiResponse>> = _response

    fun insertFee(request : InsertFeeRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.insertFee(request)
                if (response.success) {
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