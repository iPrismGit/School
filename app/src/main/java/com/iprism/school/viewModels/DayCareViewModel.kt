package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.dairy.DiaryApiRequest
import com.iprism.school.model.dairy.DiaryResponse
import com.iprism.school.model.daycare.DayCareApiRequest
import com.iprism.school.model.daycare.DayCareResponse
import com.iprism.school.repositories.DayCareRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class DayCareViewModel(private var repository: DayCareRepository) : ViewModel() {

    private val _dayCarePlansResponse = MutableLiveData<UiState<DayCareResponse>>()
    val dayCarePlansResponse: LiveData<UiState<DayCareResponse>> = _dayCarePlansResponse

    fun fetchDayCarePlans(request : DayCareApiRequest) {
        viewModelScope.launch {
            _dayCarePlansResponse.value = UiState.Loading
            try {
                val response = repository.fetchDayCarePlansAndStudents(request)
                if (response.status) {
                    _dayCarePlansResponse.value = UiState.Success(response.response)
                } else {
                    _dayCarePlansResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _dayCarePlansResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}