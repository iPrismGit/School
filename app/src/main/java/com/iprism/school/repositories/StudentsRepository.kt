package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.eventsmodel.EventsApiRequest
import com.iprism.school.model.eventsmodel.EventsApiResponse
import com.iprism.school.model.studentsmodel.StudentsApiRequest
import com.iprism.school.model.studentsmodel.StudentsApiResponse
import com.iprism.school.network.SchoolApi

class StudentsRepository(private val context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun fetchActiveStudents(request: StudentsApiRequest): StudentsApiResponse {
        var response = apiService.fetchActiveStudents(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchActiveStudents(request)
            }
        }

        return response
    }

    suspend fun fetchInActiveStudents(request: StudentsApiRequest): StudentsApiResponse {
        var response = apiService.fetchInActiveStudents(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchInActiveStudents(request)
            }
        }

        return response
    }

}