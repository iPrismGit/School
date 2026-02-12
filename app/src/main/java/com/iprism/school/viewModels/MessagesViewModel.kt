package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.albums.AlbumCoverImagesResponse
import com.iprism.school.model.helptutorials.HelpTutorialsApiRequest
import com.iprism.school.model.messagemodel.MessagesApiRequest
import com.iprism.school.model.messagemodel.MessagesApiResponse
import com.iprism.school.repositories.MessagesRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class MessagesViewModel(var repository: MessagesRepository) : ViewModel() {

    private val _messagesResponse = MutableLiveData<UiState<MessagesApiResponse>>()
    val messagesResponse: LiveData<UiState<MessagesApiResponse>> = _messagesResponse

    private val _insertMessageResponse = MutableLiveData<UiState<MessagesApiResponse>>()
    val insertMessageResponse: LiveData<UiState<MessagesApiResponse>> = _insertMessageResponse

    fun fetchChats(request: MessagesApiRequest) {
        viewModelScope.launch {
            _messagesResponse.value = UiState.Loading
            try {
                val response = repository.fetchAndInsertMessages(request)
                if (response.status) {
                    _messagesResponse.value = UiState.Success(response)
                } else {
                    _messagesResponse.value = UiState.Error(response.message)
                }
            } catch (e: Exception) {
                _messagesResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun insertMessage(request: MessagesApiRequest) {
        viewModelScope.launch {
            _insertMessageResponse.value = UiState.Loading
            try {
                val response = repository.fetchAndInsertMessages(request)
                if (response.status) {
                    _insertMessageResponse.value = UiState.Success(response)
                } else {
                    _insertMessageResponse.value = UiState.Error(response.message)
                }
            } catch (e: Exception) {
                _insertMessageResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}