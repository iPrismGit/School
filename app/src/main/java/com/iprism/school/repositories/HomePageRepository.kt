package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.homepagemodel.HomePageApiRequest
import com.iprism.school.model.homepagemodel.HomePageApiResponse
import com.iprism.school.network.SchoolApi

class HomePageRepository(private var context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun fetchHomePageDetails(request: HomePageApiRequest): HomePageApiResponse {
        var response = apiService.fetchHomePageDetails(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchHomePageDetails(request)
            }
        }

        return response
    }

}