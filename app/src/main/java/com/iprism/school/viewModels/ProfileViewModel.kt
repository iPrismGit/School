package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.profile.ProfileApiRequest
import com.iprism.school.model.profile.ProfileApiResponse
import com.iprism.school.repositories.ProfileRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class ProfileViewModel(private var repository: ProfileRepository) : ViewModel() {

    private val _profileResponse = MutableLiveData<UiState<ProfileApiResponse>>()
    val profileResponse: LiveData<UiState<ProfileApiResponse>> = _profileResponse

    fun fetchProfileDetails(request: ProfileApiRequest) {
        viewModelScope.launch {
            _profileResponse.value = UiState.Loading
            try {
                val response = repository.staffAttendanceDetails(request)
                if (response.status) {
                    _profileResponse.value = UiState.Success(response)
                } else {
                    _profileResponse.value =
                        UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _profileResponse.value =
                    UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}