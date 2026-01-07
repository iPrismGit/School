package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.albums.AlbumCoverImagesApiRequest
import com.iprism.school.model.albums.AlbumCoverImagesResponse
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherResponse
import com.iprism.school.repositories.AlbumsRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class AlbumsViewModel(private val repository: AlbumsRepository) : ViewModel() {

    private val _albumCoversResponse = MutableLiveData<UiState<AlbumCoverImagesResponse>>()
    val albumCoversResponse: LiveData<UiState<AlbumCoverImagesResponse>> = _albumCoversResponse

    fun fetchAndInsertAlbumCovers(request : AlbumCoverImagesApiRequest) {
        viewModelScope.launch {
            _albumCoversResponse.value = UiState.Loading
            try {
                val response = repository.fetchAndInsertAlbumCovers(request)
                if (response.status) {
                    _albumCoversResponse.value = UiState.Success(response.response)
                } else {
                    _albumCoversResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _albumCoversResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}