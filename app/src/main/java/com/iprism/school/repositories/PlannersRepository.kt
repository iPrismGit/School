package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.eventsmodel.EventsApiRequest
import com.iprism.school.model.eventsmodel.EventsApiResponse
import com.iprism.school.model.plannersandresources.PlannersAndResourcesApiRequest
import com.iprism.school.model.plannersandresources.PlannersAndResourcesApiResponse
import com.iprism.school.model.plannersandresources.PlannersAndResourcesResponse
import com.iprism.school.network.SchoolApi

class PlannersRepository(private val context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun fetchPlannersAndResources(request: PlannersAndResourcesApiRequest): PlannersAndResourcesApiResponse {
        var response = apiService.fetchPlannersAndResources(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchPlannersAndResources(request)
            }
        }

        return response
    }

}