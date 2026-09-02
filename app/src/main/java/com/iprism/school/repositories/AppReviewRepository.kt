package com.iprism.school.repositories

import android.content.Context
import com.iprism.parentapp.model.appreview.AppReviewApiResponse
import com.iprism.parentapp.model.appreview.AppReviewRequest
import com.iprism.school.network.SchoolApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppReviewRepository(private var context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun insertReview(request: AppReviewRequest): AppReviewApiResponse =
        withContext(Dispatchers.IO) {
            var response = apiService.insertReview(request)
            if (response.message.equals("Invalid or expired token", true)) {
                val refreshed = authRepository.refreshToken()
                if (refreshed) {
                    response = apiService.insertReview(request)
                }
            }
            response
        }
}
