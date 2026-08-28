package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.timetable.TimeTableApiResponse
import com.iprism.school.model.timetable.TimeTableRequest
import com.iprism.school.repositories.TimeTableRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class TimeTableViewModel(private val repository: TimeTableRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<TimeTableApiResponse>>()
    val response: LiveData<UiState<TimeTableApiResponse>> = _response

    fun fetchTimeTable(request : TimeTableRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchTimeTable(request)
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