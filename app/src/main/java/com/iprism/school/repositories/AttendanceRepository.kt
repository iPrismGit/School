package com.iprism.school.repositories

import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiResponse
import com.iprism.school.model.classteachermodel.ClassesApiResponse
import com.iprism.school.model.classteachermodel.SectionsApiResponse

class AttendanceRepository {

    private val apiService = SchoolApi.schoolApiService

    suspend fun getYearClassAndSection(request: ClassTeacherApiRequest): ClassTeacherApiResponse {
        return apiService.getYearClassAndSection(request)
    }

    suspend fun getClasses(request: ClassTeacherApiRequest): ClassesApiResponse {
        return apiService.getClasses(request)
    }

    suspend fun getSections(request: ClassTeacherApiRequest): SectionsApiResponse {
        return apiService.getSections(request)
    }

}