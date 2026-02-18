package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.DayCareAttendanceApiRequest
import com.iprism.school.model.daycare.DayCareApiResponse
import com.iprism.school.model.helptutorials.HelpTutorialsApiRequest
import com.iprism.school.model.helptutorials.HelpTutorialsApiResponse
import com.iprism.school.repositories.DayCareAttendanceRepository
import com.iprism.school.repositories.HelpTutorialsRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class DayCareAttendanceViewModel(private val repository: DayCareAttendanceRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<DayCareApiResponse>>()
    val response: LiveData<UiState<DayCareApiResponse>> = _response

    private val _insertDaycareAttendanceResponse = MutableLiveData<UiState<DayCareApiResponse>>()
    val insertDaycareAttendanceResponse : LiveData<UiState<DayCareApiResponse>> = _insertDaycareAttendanceResponse

    fun fetchDayCareStudents(request: DayCareAttendanceApiRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchStudentsAndInsertAttendance(request)
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

    fun insertDayCareStudentsAttendance(request: DayCareAttendanceApiRequest) {
        viewModelScope.launch {
            _insertDaycareAttendanceResponse.value = UiState.Loading
            try {
                val response = repository.fetchStudentsAndInsertAttendance(request)
                if (response.status) {
                    _insertDaycareAttendanceResponse.value = UiState.Success(response)
                } else {
                    _insertDaycareAttendanceResponse.value = UiState.Error(response.message)
                }
            } catch (e: Exception) {
                _insertDaycareAttendanceResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}