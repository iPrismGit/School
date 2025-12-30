package com.iprism.school.repositories

import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiResponse

class AttendanceRepository {

    private val apiService = SchoolApi.schoolApiService

    suspend fun getYearClassAndSection(request: ClassTeacherApiRequest): ClassTeacherApiResponse {
        return apiService.getYearClassAndSection(request)
    }

}