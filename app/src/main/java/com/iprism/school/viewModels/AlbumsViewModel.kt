package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.albums.AlbumCoverImagesApiRequest
import com.iprism.school.model.albums.AlbumCoverImagesResponse
import com.iprism.school.model.albums.AlbumsGalleryApiResponse
import com.iprism.school.model.albums.AlbumsGalleryResponse
import com.iprism.school.model.albums.DayCareAlbumsApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherResponse
import com.iprism.school.repositories.AlbumsRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AlbumsViewModel(private val repository: AlbumsRepository) : ViewModel() {

    private val _albumCoversResponse = MutableLiveData<UiState<AlbumCoverImagesResponse>>()
    val albumCoversResponse: LiveData<UiState<AlbumCoverImagesResponse>> = _albumCoversResponse

    private val _insertAlbumCoverResponse = MutableLiveData<UiState<AlbumCoverImagesResponse>>()
    val insertAlbumCoverResponse: LiveData<UiState<AlbumCoverImagesResponse>> = _insertAlbumCoverResponse

    private val _uploadMediaResponse = MutableLiveData<UiState<AlbumsGalleryResponse>>()
    val uploadMediaResponse: LiveData<UiState<AlbumsGalleryResponse>> = _uploadMediaResponse

    private val _insertImagesResponse = MutableLiveData<UiState<AlbumsGalleryResponse>>()
    val insertImagesResponse: LiveData<UiState<AlbumsGalleryResponse>> = _insertImagesResponse

    private val _dayCareAlbumCoversResponse = MutableLiveData<UiState<AlbumCoverImagesResponse>>()
    val dayCareAlbumCoversResponse: LiveData<UiState<AlbumCoverImagesResponse>> = _dayCareAlbumCoversResponse

    private val _insertDayCareAlbumCoversResponse = MutableLiveData<UiState<AlbumCoverImagesResponse>>()
    val insertDayCareAlbumCoversResponse: LiveData<UiState<AlbumCoverImagesResponse>> = _insertDayCareAlbumCoversResponse

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

    fun insertAlbumCover(request : AlbumCoverImagesApiRequest) {
        viewModelScope.launch {
            _insertAlbumCoverResponse.value = UiState.Loading
            try {
                val response = repository.fetchAndInsertAlbumCovers(request)
                if (response.status) {
                    _insertAlbumCoverResponse.value = UiState.Success(response.response)
                } else {
                    _insertAlbumCoverResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _insertAlbumCoverResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun uploadAlbumMedia(
        userId: RequestBody,
        albumId: RequestBody,
        viewType: RequestBody,
        page: RequestBody,
        type: RequestBody,
        media: List<MultipartBody.Part>
    ) {
        viewModelScope.launch {
            _uploadMediaResponse.value = UiState.Loading
            try {
                val response = repository.uploadAlbumMedia(userId, albumId, viewType, page, type, media)
                if (response.status) {
                    _uploadMediaResponse.value = UiState.Success(response.response)
                } else {
                    _uploadMediaResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _uploadMediaResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun insertAlbumMedia(
        userId: RequestBody,
        albumId: RequestBody,
        viewType: RequestBody,
        page: RequestBody,
        type: RequestBody,
        media: List<MultipartBody.Part>
    ) {
        viewModelScope.launch {
            _insertImagesResponse.value = UiState.Loading
            try {
                val response = repository.uploadAlbumMedia(userId, albumId, viewType, page, type, media)
                if (response.status) {
                    _insertImagesResponse.value = UiState.Success(response.response)
                } else {
                    _insertImagesResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _insertImagesResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchDayCareAlbumCovers(request : DayCareAlbumsApiRequest) {
        viewModelScope.launch {
            _dayCareAlbumCoversResponse.value = UiState.Loading
            try {
                val response = repository.fetchAndInsertDayCareAlbumCovers(request)
                if (response.status) {
                    _dayCareAlbumCoversResponse.value = UiState.Success(response.response)
                } else {
                    _dayCareAlbumCoversResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _dayCareAlbumCoversResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun insertDayCareAlbumCovers(request : DayCareAlbumsApiRequest) {
        viewModelScope.launch {
            _insertDayCareAlbumCoversResponse.value = UiState.Loading
            try {
                val response = repository.fetchAndInsertDayCareAlbumCovers(request)
                if (response.status) {
                    _insertDayCareAlbumCoversResponse.value = UiState.Success(response.response)
                } else {
                    _insertDayCareAlbumCoversResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _insertDayCareAlbumCoversResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}