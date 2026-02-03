package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.homepagemodel.HomePageApiRequest
import com.iprism.school.model.homepagemodel.HomePageResponse
import com.iprism.school.repositories.HomePageRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class HomePageViewModel(private var repository: HomePageRepository) : ViewModel() {

    private val _homePageResponse = MutableLiveData<UiState<HomePageResponse>>()
    val homePageResponse: LiveData<UiState<HomePageResponse>> = _homePageResponse

    fun fetchAndInsertAlbumCovers(request : HomePageApiRequest) {
        viewModelScope.launch {
            _homePageResponse.value = UiState.Loading
            try {
                val response = repository.fetchHomePageDetails(request)
                if (response.status) {
                    _homePageResponse.value = UiState.Success(response.response)
                } else {
                    _homePageResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _homePageResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}