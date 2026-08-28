package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.timetable.TimeTableApiResponse
import com.iprism.school.model.timetable.TimeTableRequest
import com.iprism.school.network.SchoolApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TimeTableRepository(private var context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun fetchTimeTable(request: TimeTableRequest): TimeTableApiResponse =
        withContext(Dispatchers.IO) {
            var response = apiService.fetchTimeTable(request)
            if (response.message.equals("Invalid or expired token", true)) {
                val refreshed = authRepository.refreshToken()
                if (refreshed) {
                    response = apiService.fetchTimeTable(request)
                }
            }
            response
        }
}
