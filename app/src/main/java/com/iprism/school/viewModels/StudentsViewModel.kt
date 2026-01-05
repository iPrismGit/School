package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.studentsmodel.StudentsApiRequest
import com.iprism.school.model.studentsmodel.StudentsResponse
import com.iprism.school.repositories.StudentsRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class StudentsViewModel(private val repository: StudentsRepository) : ViewModel() {

    private val _activeStudentsResponse = MutableLiveData<UiState<StudentsResponse>>()
    val activeStudentsResponse: LiveData<UiState<StudentsResponse>> = _activeStudentsResponse

    private val _inActiveStudentsResponse = MutableLiveData<UiState<StudentsResponse>>()
    val inActiveStudentsResponse: LiveData<UiState<StudentsResponse>> = _inActiveStudentsResponse

    fun fetchActiveStudents(request : StudentsApiRequest) {
        viewModelScope.launch {
            _activeStudentsResponse.value = UiState.Loading
            try {
                val response = repository.fetchActiveStudents(request)
                if (response.status) {
                    _activeStudentsResponse.value = UiState.Success(response.response)
                } else {
                    _activeStudentsResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _activeStudentsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }


    fun fetchInActiveStudents(request : StudentsApiRequest) {
        viewModelScope.launch {
            _inActiveStudentsResponse.value = UiState.Loading
            try {
                val response = repository.fetchInActiveStudents(request)
                if (response.status) {
                    _inActiveStudentsResponse.value = UiState.Success(response.response)
                } else {
                    _inActiveStudentsResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _inActiveStudentsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}