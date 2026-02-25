package com.iprism.school.model.classteachermodel

data class AttendanceStudentsApiRequest(

    val academic_year: String,
    val attendance_id: String,
    val attendance_status: String,
    val branch_id: String,
    val class_id: String,
    val date: String,
    val section_id: String,
    val students: List<AttendanceStudent>,
    val user_id: String,
    val view_type: String,
    val student_type: String,
    val page : Int
)

data class AttendanceStudent(

    val id: Int

)