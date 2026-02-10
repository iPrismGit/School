package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.staffattendacemodel.StaffAttendanceApiRequest
import com.iprism.school.model.staffattendacemodel.StaffAttendanceResponse
import com.iprism.school.repositories.StaffAttendanceApiRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class StaffAttendanceViewModel(private var repository: StaffAttendanceApiRepository) : ViewModel() {

    private val _attendanceDetailsResponse = MutableLiveData<UiState<StaffAttendanceResponse>>()
    val attendanceDetailsResponse: LiveData<UiState<StaffAttendanceResponse>> = _attendanceDetailsResponse

    private val _insertAttendanceResponse = MutableLiveData<UiState<StaffAttendanceResponse>>()
    val insertAttendanceResponse: LiveData<UiState<StaffAttendanceResponse>> = _insertAttendanceResponse

    fun staffAttendanceDetails(request: StaffAttendanceApiRequest) {
        viewModelScope.launch {
            _attendanceDetailsResponse.value = UiState.Loading
            try {
                val response = repository.staffAttendanceDetails(request)
                if (response.status) {
                    _attendanceDetailsResponse.value = UiState.Success(response.response)
                } else {
                    _attendanceDetailsResponse.value =
                        UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _attendanceDetailsResponse.value =
                    UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun insertStaffAttendance(request: StaffAttendanceApiRequest) {
        viewModelScope.launch {
            _insertAttendanceResponse.value = UiState.Loading
            try {
                val response = repository.staffAttendanceDetails(request)
                if (response.status) {
                    _insertAttendanceResponse.value = UiState.Success(response.response)
                } else {
                    _insertAttendanceResponse.value =
                        UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _insertAttendanceResponse.value =
                    UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}