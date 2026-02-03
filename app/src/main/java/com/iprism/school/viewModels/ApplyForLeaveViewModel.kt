package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.applyforleavemodel.ApplyForLeaveApiRequest
import com.iprism.school.model.applyforleavemodel.ApplyForLeaveResponse
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherResponse
import com.iprism.school.repositories.ApplyForLeaveRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class ApplyForLeaveViewModel(private var repository: ApplyForLeaveRepository) : ViewModel() {

    private val _insertLeaveRequestResponse = MutableLiveData<UiState<ApplyForLeaveResponse>>()
    val insertLeaveRequestResponse: LiveData<UiState<ApplyForLeaveResponse>> = _insertLeaveRequestResponse

    private val _leaveRequestsResponse = MutableLiveData<UiState<ApplyForLeaveResponse>>()
    val leaveRequestsResponse: LiveData<UiState<ApplyForLeaveResponse>> = _leaveRequestsResponse

    fun insertLeaveRequest(request : ApplyForLeaveApiRequest) {
        viewModelScope.launch {
            _insertLeaveRequestResponse.value = UiState.Loading
            try {
                val response = repository.leaveRequestDetails(request)
                if (response.status) {
                    _insertLeaveRequestResponse.value = UiState.Success(response.response)
                } else {
                    _insertLeaveRequestResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _insertLeaveRequestResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchLeaveRequests(request : ApplyForLeaveApiRequest) {
        viewModelScope.launch {
            _leaveRequestsResponse.value = UiState.Loading
            try {
                val response = repository.leaveRequestDetails(request)
                if (response.status) {
                    _leaveRequestsResponse.value = UiState.Success(response.response)
                } else {
                    _leaveRequestsResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _leaveRequestsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}