package com.iprism.school.repositories

import com.iprism.school.model.classteachermodel.AttendanceStudentsApiRequest
import com.iprism.school.model.classteachermodel.AttendanceStudentsApiResponse
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiResponse
import com.iprism.school.model.classteachermodel.ClassesApiResponse
import com.iprism.school.model.classteachermodel.SectionsApiResponse

class AttendanceRepository {

    private val apiService = SchoolApi.schoolApiService

    suspend fun getYearClassAndSection(request: ClassTeacherApiRequest): ClassTeacherApiResponse {
        return apiService.getYearClassAndSection(request)
    }

    suspend fun getStudents(request: AttendanceStudentsApiRequest): AttendanceStudentsApiResponse {
        return apiService.getStudents(request)
    }

}