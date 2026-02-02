package com.iprism.school.model.staffattendacemodel

data class StaffAttendanceApiResponse(

    val message: String,
    val response: StaffAttendanceResponse,
    val status: Boolean

)

data class StaffAttendanceResponse(

    val absent_days: String,
    val attendance: List<Attendance>,
    val present_days: String,
    val holidays: String,
    val total_days: String

)

data class Attendance(

    val date: String,
    val in_time: String,
    val out_time: String,
    val status: String,
    val time_type: String

)