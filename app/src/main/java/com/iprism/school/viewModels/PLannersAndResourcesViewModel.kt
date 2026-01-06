package com.iprism.school.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.circularmodels.CircularApiRequest
import com.iprism.school.model.circularmodels.CircularResponse
import com.iprism.school.model.plannersandresources.PlannersAndResourcesApiRequest
import com.iprism.school.model.plannersandresources.PlannersAndResourcesResponse
import com.iprism.school.repositories.CircularRepository
import com.iprism.school.repositories.PlannersRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class PLannersAndResourcesViewModel(private val repository: PlannersRepository) : ViewModel() {

    private val _plannerCategoriesResponse = MutableLiveData<UiState<PlannersAndResourcesResponse>>()
    val plannerCategoriesResponse: LiveData<UiState<PlannersAndResourcesResponse>> = _plannerCategoriesResponse

    fun fetchPlannerCategories(request : PlannersAndResourcesApiRequest) {
        viewModelScope.launch {
            _plannerCategoriesResponse.value = UiState.Loading
            try {
                val response = repository.fetchPlannersAndResources(request)
                if (response.status) {
                    _plannerCategoriesResponse.value = UiState.Success(response.response)
                } else {
                    _plannerCategoriesResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _plannerCategoriesResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}