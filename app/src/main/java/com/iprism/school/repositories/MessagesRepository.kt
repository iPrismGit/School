package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.messagemodel.MessagesApiRequest
import com.iprism.school.model.messagemodel.MessagesApiResponse
import com.iprism.school.network.SchoolApi

class MessagesRepository(private var context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun fetchAndInsertMessages(request: MessagesApiRequest): MessagesApiResponse {
        var response = apiService.fetchAndInsertMessages(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchAndInsertMessages(request)
            }
        }

        return response
    }

}