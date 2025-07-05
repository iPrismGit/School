package com.iprism.school.model.Response

data class AttandanceStudentResponse(
    val message: String,
    val response: ResponseStudentAttandance,
    val status: Boolean
)

data class ResponseStudentAttandance(
    val attendance: List<AttendanceStudents>
)

data class AttendanceStudents(
    val attendance_status: String,
    val student_id: String,
    val student_name: String,
    val student_image: String,
    val total_absent_students: String,
    val total_present_students: String
)