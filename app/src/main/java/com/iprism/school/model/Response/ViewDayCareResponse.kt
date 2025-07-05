package com.iprism.school.model.Response

data class ViewDayCareResponse(
    val message: String,
    val response: ResponseNewwwww,
    val status: Boolean
)

data class ResponseNewwwww(
    val attachments: List<Any>,
    val student_daycare_reports: List<StudentDaycareReport>
)

data class StudentDaycareReport(
    val activity_name: String,
    val attachment: String,
    val created_on: String,
    val date: String,
    val delete_status: String,
    val edit: String,
    val end_time: String,
    val group_id: String,
    val id: String,
    val meal_name: String,
    val notify_parent: String,
    val quantity: String,
    val remarks: String,
    val report_type: String,
    val school_id: String,
    val staff_id: String,
    val start_time: String,
    val updated_on: Any
)