package com.iprism.school.repositories

import android.content.Context

import com.iprism.school.model.eventsmodel.EventsApiRequest
import com.iprism.school.model.eventsmodel.EventsApiResponse
import com.iprism.school.network.SchoolApi

class EventsRepository(private val context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun getEvents(request: EventsApiRequest): EventsApiResponse {
        var response = apiService.fetchEvents(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchEvents(request)
            }
        }

        return response
    }

}