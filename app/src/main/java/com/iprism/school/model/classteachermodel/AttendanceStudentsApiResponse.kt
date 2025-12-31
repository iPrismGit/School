package com.iprism.school.model.classteachermodel

data class AttendanceStudentsApiResponse(

    val message: String,
    val response: AttendanceStudentsResponse,
    val status: Boolean

)

data class AttendanceStudentsResponse(

    val attendance_status: String,
    val students: List<Student>

)

data class Student(

    val academic_year: Int,
    val attendance_id: Int,
    val attendance_status: String,
    val child_image: String,
    val class_id: Int,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val middle_name: String,
    val section_id: Int

)
