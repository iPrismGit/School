package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.helptutorials.HelpTutorialsApiRequest
import com.iprism.school.model.helptutorials.HelpTutorialsApiResponse
import com.iprism.school.network.SchoolApi

class HelpTutorialsRepository(private var context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun fetchHelpTutorials(request: HelpTutorialsApiRequest): HelpTutorialsApiResponse {
        var response = apiService.fetchHelpTutorials(request)
        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchHelpTutorials(request)
            }
        }
        return response
    }

}