package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiResponse
import com.iprism.school.model.contentpagesmodel.ContentPagesApiRequest
import com.iprism.school.model.contentpagesmodel.ContentPagesApiResponse
import com.iprism.school.model.contentpagesmodel.SchoolSupportApiRequest
import com.iprism.school.model.contentpagesmodel.SchoolSupportApiResponse
import com.iprism.school.network.SchoolApi

class ContentPagesRepository(private val context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun fetchAppContent(request: ContentPagesApiRequest): ContentPagesApiResponse {
        var response = apiService.fetchAppContent(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchAppContent(request)
            }
        }
        return response
    }

    suspend fun fetchSchoolSupportDetails(request: SchoolSupportApiRequest): SchoolSupportApiResponse {
        var response = apiService.fetchSchoolSupportDetails(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchSchoolSupportDetails(request)
            }
        }
        return response
    }

    suspend fun fetchTechnicalSupportDetails(request: SchoolSupportApiRequest): SchoolSupportApiResponse {
        var response = apiService.fetchTechnicalSupportDetails(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchTechnicalSupportDetails(request)
            }
        }
        return response
    }

}