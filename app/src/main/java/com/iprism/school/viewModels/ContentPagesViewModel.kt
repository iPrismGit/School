package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.contentpagesmodel.ContentPagesApiRequest
import com.iprism.school.model.contentpagesmodel.ContentPagesResponse
import com.iprism.school.model.contentpagesmodel.SchoolSupportApiRequest
import com.iprism.school.model.contentpagesmodel.SchoolSupportApiResponse
import com.iprism.school.repositories.ContentPagesRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class ContentPagesViewModel(private  val repository: ContentPagesRepository ) : ViewModel() {

    private val _contentPagesResponse = MutableLiveData<UiState<ContentPagesResponse>>()
    val contentPagesResponse: LiveData<UiState<ContentPagesResponse>> = _contentPagesResponse

    private val _schoolSupportResponse = MutableLiveData<UiState<SchoolSupportApiResponse>>()
    val schoolSupportResponse: LiveData<UiState<SchoolSupportApiResponse>> = _schoolSupportResponse

    fun fetchAppContent(request : ContentPagesApiRequest) {
        viewModelScope.launch {
            _contentPagesResponse.value = UiState.Loading
            try {
                val response = repository.fetchAppContent(request)
                if (response.status) {
                    _contentPagesResponse.value = UiState.Success(response.response)
                } else {
                    _contentPagesResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _contentPagesResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchSchoolSupportDetails(request : SchoolSupportApiRequest) {
        viewModelScope.launch {
            _schoolSupportResponse.value = UiState.Loading
            try {
                val response = repository.fetchSchoolSupportDetails(request)
                if (response.status) {
                    _schoolSupportResponse.value = UiState.Success(response)
                } else {
                    _schoolSupportResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _schoolSupportResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}