package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.circularmodels.CircularApiRequest
import com.iprism.school.model.circularmodels.CircularApiResponse
import com.iprism.school.model.dairy.DiaryApiRequest
import com.iprism.school.model.dairy.DiaryApiResponse
import com.iprism.school.network.SchoolApi

class DiaryRepository(private var context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun fetchAndInsertDiaries(request: DiaryApiRequest): DiaryApiResponse {
        var response = apiService.fetchDiaryAndInsert(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchDiaryAndInsert(request)
            }
        }

        return response
    }

}