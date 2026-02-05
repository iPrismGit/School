package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.helptutorials.HelpTutorialsApiRequest
import com.iprism.school.model.helptutorials.HelpTutorialsApiResponse
import com.iprism.school.repositories.HelpTutorialsRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class HelpTutorialsViewModel(private val repository: HelpTutorialsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<HelpTutorialsApiResponse>>()
    val response: LiveData<UiState<HelpTutorialsApiResponse>> = _response

    fun fetchHelpTutorials(request: HelpTutorialsApiRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchHelpTutorials(request)
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

}
