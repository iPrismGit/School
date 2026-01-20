package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.eventsmodel.EventsApiRequest
import com.iprism.school.model.eventsmodel.EventsResponse
import com.iprism.school.model.holidaysmodel.HolidaysApiRequest
import com.iprism.school.model.holidaysmodel.HolidaysApiResponse
import com.iprism.school.model.holidaysmodel.HolidaysResponse
import com.iprism.school.repositories.EventsRepository
import com.iprism.school.repositories.HolidaysRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class HolidaysViewModel(private var repository: HolidaysRepository) : ViewModel() {

    private val _holidaysResponse = MutableLiveData<UiState<HolidaysResponse>>()
    val holidaysResponse: LiveData<UiState<HolidaysResponse>> = _holidaysResponse


    fun fetchHolidays(request : HolidaysApiRequest) {
        viewModelScope.launch {
            _holidaysResponse.value = UiState.Loading
            try {
                val response = repository.getHolidaysCalender(request)
                if (response.status) {
                    _holidaysResponse.value = UiState.Success(response.response)
                } else {
                    _holidaysResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _holidaysResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}