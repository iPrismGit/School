package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.circularmodels.CircularApiRequest
import com.iprism.school.model.circularmodels.CircularApiResponse
import com.iprism.school.network.SchoolApi

class CircularRepository (private val context: Context){

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun getCirculars(request: CircularApiRequest): CircularApiResponse {
        var response = apiService.fetchCirculars(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchCirculars(request)
            }
        }

        return response
    }

}