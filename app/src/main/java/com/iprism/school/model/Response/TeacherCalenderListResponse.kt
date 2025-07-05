package com.iprism.school.model.Response

data class TeacherCalenderListResponse(
    val message: String,
    val response: ResponseCal_list,
    val status: Boolean
)

data class ResponseCal_list(
    val calender_details: List<CalenderDetailListnn>
)

data class CalenderDetailListnn(
    val advance_options: String,
    val attachment: String,
    val attachment_type: String,
    val calender_date: String,
    val class_ids: String,
    val class_names: String,
    val created_on: String,
    val date: String,
    val day: String,
    val delete_status: String,
    val details: String,
    val group_ids: String,
    val group_names: String,
    val id: String,
    val location: String,
    val school_id: String,
    val staff_ids: String,
    val staff_names: String,
    val student_ids: String,
    val student_names: String,
    val subject: String,
    val teacher_id: String,
    val time: String,
    val updated_on: Any
)