package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.messagemodel.DayCareMessagesApiRequest
import com.iprism.school.model.messagemodel.MessagesApiRequest
import com.iprism.school.model.messagemodel.MessagesApiResponse
import com.iprism.school.network.SchoolApi

class DayCareMessagesRepository(private val context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun fetchAndInsertDayCareMessages(request: DayCareMessagesApiRequest): MessagesApiResponse {
        var response = apiService.fetchAndInsertDaycareMessages(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchAndInsertDaycareMessages(request)
            }
        }

        return response
    }

}