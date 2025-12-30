package com.iprism.school.repositories

import com.iprism.school.model.authmodel.LoginApiRequest
import com.iprism.school.model.authmodel.LoginApiResponse
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiResponse

class AuthenticationRepository {

    private val apiService = SchoolApi.schoolApiService

    suspend fun userLogin(request: LoginApiRequest): LoginApiResponse {
        return apiService.userLogin(request)
    }

}