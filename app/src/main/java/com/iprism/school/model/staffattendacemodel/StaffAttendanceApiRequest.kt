package com.iprism.school.model.staffattendacemodel

data class StaffAttendanceApiRequest(

    val academic_year: String,
    val branch_id: String,
    val date: String,
    val lat: String,
    val lon: String,
    val month: String,
    val status: String,
    val time: String,
    val time_type: String,
    val user_id: String,
    val view_type: String,
    val year: String

)