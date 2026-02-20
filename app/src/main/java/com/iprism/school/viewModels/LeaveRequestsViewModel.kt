package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.homepagemodel.HomePageApiRequest
import com.iprism.school.model.homepagemodel.HomePageResponse
import com.iprism.school.model.leaverequestmodel.LeaveRequestApiRequest
import com.iprism.school.model.leaverequestmodel.LeaveRequestApiResponse
import com.iprism.school.model.leaverequestmodel.LeaveRequestResponse
import com.iprism.school.repositories.HomePageRepository
import com.iprism.school.repositories.LeaveRequestRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class LeaveRequestsViewModel(private var repository: LeaveRequestRepository) : ViewModel() {

    private val _leaveRequestsResponse = MutableLiveData<UiState<LeaveRequestApiResponse>>()
    val leaveRequestsResponse: LiveData<UiState<LeaveRequestApiResponse>> = _leaveRequestsResponse

    private val _updateLeaveRequestsResponse = MutableLiveData<UiState<LeaveRequestApiResponse>>()
    val updateLeaveRequestsResponse: LiveData<UiState<LeaveRequestApiResponse>> = _updateLeaveRequestsResponse

    private val _leaveRequestsCountResponse = MutableLiveData<UiState<LeaveRequestApiResponse>>()
    val leaveRequestsCountResponse: LiveData<UiState<LeaveRequestApiResponse>> = _leaveRequestsCountResponse

    fun fetchLeaveRequests(request: LeaveRequestApiRequest) {
        viewModelScope.launch {
            _leaveRequestsResponse.value = UiState.Loading
            try {
                val response = repository.fetchLeaveRequests(request)
                if (response.status) {
                    _leaveRequestsResponse.value = UiState.Success(response)
                } else {
                    _leaveRequestsResponse.value =
                        UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _leaveRequestsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun updateLeaveRequests(request: LeaveRequestApiRequest) {
        viewModelScope.launch {
            _updateLeaveRequestsResponse.value = UiState.Loading
            try {
                val response = repository.fetchLeaveRequests(request)
                if (response.status) {
                    _updateLeaveRequestsResponse.value = UiState.Success(response)
                } else {
                    _updateLeaveRequestsResponse.value =
                        UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _updateLeaveRequestsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchLeaveRequestsCount(request: LeaveRequestApiRequest) {
        viewModelScope.launch {
            _leaveRequestsCountResponse.value = UiState.Loading
            try {
                val response = repository.fetchLeaveRequests(request)
                if (response.status) {
                    _leaveRequestsCountResponse.value = UiState.Success(response)
                } else {
                    _leaveRequestsCountResponse.value =
                        UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _leaveRequestsCountResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}