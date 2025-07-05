package com.iprism.school.model.Response

data class StaffAttandanceResponse(
    val message: String,
    val response: ResponseListttt_lisy,
    val status: Boolean
)

data class ResponseListttt_lisy(
    val attendance: List<AttendanceLsittt>
)

data class AttendanceLsittt(
    val employee_designation: String,
    val employee_name: String,
    val in_time: String,
    val intime_distance: String,
    val out_time: String,
    val outtime_distance: String
)