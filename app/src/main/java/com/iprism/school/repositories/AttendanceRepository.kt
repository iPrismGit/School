package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.classteachermodel.AttendanceStudentsApiRequest
import com.iprism.school.model.classteachermodel.AttendanceStudentsApiResponse
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiResponse
import com.iprism.school.network.SchoolApi

class AttendanceRepository(private val context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun getYearClassAndSection(request: ClassTeacherApiRequest): ClassTeacherApiResponse {
        var response = apiService.getYearClassAndSection(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.getYearClassAndSection(request)
            }
        }

        return response
    }

    suspend fun getStudents(request: AttendanceStudentsApiRequest): AttendanceStudentsApiResponse {
        var response = apiService.getStudents(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.getStudents(request)
            }
        }

        return response
    }

}
