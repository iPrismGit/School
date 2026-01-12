package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.dairy.DiaryApiRequest
import com.iprism.school.model.dairy.DiaryApiResponse
import com.iprism.school.model.daycare.DayCareApiRequest
import com.iprism.school.model.daycare.DayCareApiResponse
import com.iprism.school.network.SchoolApi

class DayCareRepository(private var context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun fetchDayCarePlansAndStudents(request: DayCareApiRequest): DayCareApiResponse {
        var response = apiService.fetchDayCarePlansAndStudents(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchDayCarePlansAndStudents(request)
            }
        }

        return response
    }

}