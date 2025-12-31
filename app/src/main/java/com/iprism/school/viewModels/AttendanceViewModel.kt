package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.classteachermodel.AttendanceStudentsApiRequest
import com.iprism.school.model.classteachermodel.AttendanceStudentsResponse
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherResponse
import com.iprism.school.repositories.AttendanceRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class AttendanceViewModel(private var repository: AttendanceRepository) : ViewModel() {

    private val _academicYearsResponse = MutableLiveData<UiState<ClassTeacherResponse>>()
    val academicYearsResponse: LiveData<UiState<ClassTeacherResponse>> = _academicYearsResponse

    private val _classesResponse = MutableLiveData<UiState<ClassTeacherResponse>>()
    val classesResponse: LiveData<UiState<ClassTeacherResponse>> = _classesResponse

    private val _sectionsResponse = MutableLiveData<UiState<ClassTeacherResponse>>()
    val sectionsResponse: LiveData<UiState<ClassTeacherResponse>> = _sectionsResponse

    private val _studentsResponse = MutableLiveData<UiState<AttendanceStudentsResponse>>()
    val studentsResponse: LiveData<UiState<AttendanceStudentsResponse>> = _studentsResponse

    fun fetchAcademicYears(request : ClassTeacherApiRequest) {
        viewModelScope.launch {
            _academicYearsResponse.value = UiState.Loading
            try {
                val response = repository.getYearClassAndSection(request)
                if (response.status) {
                    _academicYearsResponse.value = UiState.Success(response.response)
                } else {
                    _academicYearsResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _academicYearsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchClasses(request : ClassTeacherApiRequest) {
        viewModelScope.launch {
            _classesResponse.value = UiState.Loading
            try {
                val response = repository.getYearClassAndSection(request)
                if (response.status) {
                    _classesResponse.value = UiState.Success(response.response)
                } else {
                    _classesResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _classesResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchSections(request : ClassTeacherApiRequest) {
        viewModelScope.launch {
            _sectionsResponse.value = UiState.Loading
            try {
                val response = repository.getYearClassAndSection(request)
                if (response.status) {
                    _sectionsResponse.value = UiState.Success(response.response)
                } else {
                    _sectionsResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _sectionsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchStudents(request : AttendanceStudentsApiRequest) {
        viewModelScope.launch {
            _studentsResponse.value = UiState.Loading
            try {
                val response = repository.getStudents(request)
                if (response.status) {
                    _studentsResponse.value = UiState.Success(response.response)
                } else {
                    _studentsResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _studentsResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}