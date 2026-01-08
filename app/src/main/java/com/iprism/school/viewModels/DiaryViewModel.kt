package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.dairy.DiaryApiRequest
import com.iprism.school.model.dairy.DiaryResponse
import com.iprism.school.repositories.DiaryRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class DiaryViewModel(private var repository: DiaryRepository) : ViewModel() {

    private val _diaryResponse = MutableLiveData<UiState<DiaryResponse>>()
    val diaryResponse: LiveData<UiState<DiaryResponse>> = _diaryResponse

    private val _deleteDiaryResponse = MutableLiveData<UiState<DiaryResponse>>()
    val deleteDiaryResponse: LiveData<UiState<DiaryResponse>> = _deleteDiaryResponse

    fun fetchDiaries(request : DiaryApiRequest) {
        viewModelScope.launch {
            _diaryResponse.value = UiState.Loading
            try {
                val response = repository.fetchAndInsertDiaries(request)
                if (response.status) {
                    _diaryResponse.value = UiState.Success(response.response)
                } else {
                    _diaryResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _diaryResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun deleteDiary(request : DiaryApiRequest) {
        viewModelScope.launch {
            _deleteDiaryResponse.value = UiState.Loading
            try {
                val response = repository.fetchAndInsertDiaries(request)
                if (response.status) {
                    _deleteDiaryResponse.value = UiState.Success(response.response)
                } else {
                    _deleteDiaryResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _deleteDiaryResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}