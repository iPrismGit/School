package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.authmodel.LoginApiRequest
import com.iprism.school.model.authmodel.LoginApiResponse
import com.iprism.school.network.SchoolApi

class AuthenticationRepository(private val context: Context) {

    private val apiService = SchoolApi.create(context)

    suspend fun userLogin(request: LoginApiRequest): LoginApiResponse {
        return apiService.userLogin(request)
    }

}