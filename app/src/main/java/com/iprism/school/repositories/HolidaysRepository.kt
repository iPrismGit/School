package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.eventsmodel.EventsApiRequest
import com.iprism.school.model.eventsmodel.EventsApiResponse
import com.iprism.school.model.holidaysmodel.HolidaysApiRequest
import com.iprism.school.model.holidaysmodel.HolidaysApiResponse
import com.iprism.school.network.SchoolApi

class HolidaysRepository(private val context: Context)  {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun getHolidaysCalender(request: HolidaysApiRequest): HolidaysApiResponse {
        var response = apiService.fetchHolidays(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchHolidays(request)
            }
        }

        return response
    }

}