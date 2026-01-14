package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.daycare.DayCareApiRequest
import com.iprism.school.model.daycare.DayCareResponse
import com.iprism.school.model.daycare.DayCareStatusApiRequest
import com.iprism.school.model.daycare.DayCareStatusResponse
import com.iprism.school.repositories.DayCareRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class DayCareViewModel(private var repository: DayCareRepository) : ViewModel() {

    private val _dayCareStatusResponse = MutableLiveData<UiState<DayCareStatusResponse>>()
    val dayCareStatusResponse: LiveData<UiState<DayCareStatusResponse>> = _dayCareStatusResponse

    private val _dayCarePlansResponse = MutableLiveData<UiState<DayCareResponse>>()
    val dayCarePlansResponse: LiveData<UiState<DayCareResponse>> = _dayCarePlansResponse

    private val _dayCareStudentsResponse = MutableLiveData<UiState<DayCareResponse>>()
    val dayCareStudentsResponse: LiveData<UiState<DayCareResponse>> = _dayCareStudentsResponse

    private val _insertDayCareReportResponse = MutableLiveData<UiState<DayCareResponse>>()
    val insertDayCareReportResponse: LiveData<UiState<DayCareResponse>> =
        _insertDayCareReportResponse

    fun fetchDayCareStatus(request: DayCareStatusApiRequest) {
        viewModelScope.launch {
            _dayCareStatusResponse.value = UiState.Loading
            try {
                val response = repository.fetchDayCareStatus(request)
                if (response.status) {
                    _dayCareStatusResponse.value = UiState.Success(response.response)
                } else {
                    _dayCareStatusResponse.value =
                        UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _dayCareStatusResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchDayCarePlans(request: DayCareApiRequest) {
        viewModelScope.launch {
            _dayCarePlansResponse.value = UiState.Loading
            try {
                val response = repository.fetchDayCarePlansAndStudents(request)
                if (response.status) {
                    _dayCarePlansResponse.value = UiState.Success(response.response)
                } else {
                    _dayCarePlansResponse.value =
                        UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _dayCarePlansResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchDayCareStudents(request: DayCareApiRequest) {
        viewModelScope.launch {
            _dayCareStudentsResponse.value = UiState.Loading
            try {
                val response = repository.fetchDayCarePlansAndStudents(request)
                if (response.status) {
                    _dayCareStudentsResponse.value = UiState.Success(response.response)
                } else {
                    _dayCareStudentsResponse.value =
                        UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _dayCareStudentsResponse.value =
                    UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun insertDaycareReport(request: DayCareApiRequest) {
        viewModelScope.launch {
            _insertDayCareReportResponse.value = UiState.Loading
            try {
                val response = repository.fetchDayCarePlansAndStudents(request)
                if (response.status) {
                    _insertDayCareReportResponse.value = UiState.Success(response.response)
                } else {
                    _insertDayCareReportResponse.value =
                        UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _insertDayCareReportResponse.value =
                    UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}