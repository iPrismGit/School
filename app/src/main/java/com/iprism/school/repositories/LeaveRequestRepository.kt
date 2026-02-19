package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.albums.AlbumCoverImagesApiRequest
import com.iprism.school.model.albums.AlbumCoverImagesApiResponse
import com.iprism.school.model.leaverequestmodel.LeaveRequestApiRequest
import com.iprism.school.model.leaverequestmodel.LeaveRequestApiResponse
import com.iprism.school.network.SchoolApi

class LeaveRequestRepository(private var context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun fetchLeaveRequests(request: LeaveRequestApiRequest): LeaveRequestApiResponse {
        var response = apiService.studentLeaveRequests(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.studentLeaveRequests(request)
            }
        }

        return response
    }

}