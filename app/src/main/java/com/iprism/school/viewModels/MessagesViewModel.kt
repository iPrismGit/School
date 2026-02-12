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

    private val _response = MutableLiveData<UiState<MessagesApiResponse>>()
    val response: LiveData<UiState<MessagesApiResponse>> = _response

    private val _newResponse = MutableLiveData<UiState<MessagesApiResponse>>()
    val newResponse: LiveData<UiState<MessagesApiResponse>> = _newResponse

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

    fun fetchMessages(request : MessagesApiRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchAndInsertMessages(request)
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

    fun fetchNewMessages(request : MessagesApiRequest) {
        viewModelScope.launch {
            _newResponse.value = UiState.Loading
            try {
                val response = repository.fetchAndInsertMessages(request)
                if (response.status) {
                    _newResponse.value = UiState.Success(response)
                } else {
                    _newResponse.value = UiState.Error(response.message)
                }
            } catch (e: Exception) {
                _newResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}