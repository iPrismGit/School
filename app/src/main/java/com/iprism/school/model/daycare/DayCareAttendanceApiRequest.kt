package com.iprism.school.model.daycare

data class DayCareAttendanceApiRequest(

    val attendance_id: String,
    val attendance_status: String,
    val branch_id: String,
    val cat_id: String,
    val date: String,
    val page: Int,
    val student_type: String,
    val students: List<SelectedStudent>,
    val user_id: String,
    val view_type: String,
    val notify_parent: String

)

data class SelectedStudent(

    val id: Int

)