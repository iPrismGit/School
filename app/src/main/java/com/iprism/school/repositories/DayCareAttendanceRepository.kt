package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.DayCareAttendanceApiRequest
import com.iprism.school.model.daycare.DayCareApiResponse
import com.iprism.school.network.SchoolApi

class DayCareAttendanceRepository(private val context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun fetchStudentsAndInsertAttendance(request: DayCareAttendanceApiRequest): DayCareApiResponse {
        var response = apiService.fetchDAyCareStudentsAndInsertAttendance(request)
        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchDAyCareStudentsAndInsertAttendance(request)
            }
        }
        return response
    }

}