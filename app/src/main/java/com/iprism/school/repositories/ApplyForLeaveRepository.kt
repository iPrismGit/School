package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.applyforleavemodel.ApplyForLeaveApiRequest
import com.iprism.school.model.applyforleavemodel.ApplyForLeaveApiResponse
import com.iprism.school.network.SchoolApi

class ApplyForLeaveRepository(private var context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun leaveRequestDetails(request: ApplyForLeaveApiRequest): ApplyForLeaveApiResponse {
        var response = apiService.leaveRequestDetails(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.leaveRequestDetails(request)
            }
        }

        return response
    }

}