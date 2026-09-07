package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.parentapp.model.notifications.NotificationsApiResponse
import com.iprism.school.model.notifications.NotificationsRequest
import com.iprism.school.repositories.ProfileRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class NotificationsViewModel(private val repository: ProfileRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<NotificationsApiResponse>>()
    val response: LiveData<UiState<NotificationsApiResponse>> = _response

    fun fetchNotifications(request: NotificationsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchNotifications(request)
                if (response.status) {
                    _response.value = UiState.Success(response)
                } else {
                    _response.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _response.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}