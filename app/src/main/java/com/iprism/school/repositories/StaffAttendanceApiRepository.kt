package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.staffattendacemodel.StaffAttendanceApiRequest
import com.iprism.school.model.staffattendacemodel.StaffAttendanceApiResponse
import com.iprism.school.network.SchoolApi

class StaffAttendanceApiRepository(private val context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun staffAttendanceDetails(request: StaffAttendanceApiRequest): StaffAttendanceApiResponse {
        var response = apiService.staffAttendanceDetails(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.staffAttendanceDetails(request)
            }
        }

        return response
    }

}