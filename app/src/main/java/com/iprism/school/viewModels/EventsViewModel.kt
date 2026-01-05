package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.eventsmodel.EventsApiRequest
import com.iprism.school.model.eventsmodel.EventsResponse
import com.iprism.school.repositories.EventsRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class EventsViewModel(private var repository: EventsRepository) : ViewModel() {

    private val _eventsResponse = MutableLiveData<UiState<EventsResponse>>()
    val eventsResponse: LiveData<UiState<EventsResponse>> = _eventsResponse


    fun fetchEvents(request : EventsApiRequest) {
        viewModelScope.launch {
            _eventsResponse.value = UiState.Loading
            try {
                val response = repository.getEvents(request)
                if (response.status) {
                    _eventsResponse.value = UiState.Success(response.response)
                } else {
                    _eventsResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _eventsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}