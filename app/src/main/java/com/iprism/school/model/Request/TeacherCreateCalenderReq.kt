package com.iprism.school.model.Request

data class TeacherCreateCalenderReq(
    val advance_options: String,
    val attachment: String,
    val auth_token: String,
    val class_ids: String,
    val date: String,
    val details: String,
    val group_ids: String,
    val location: String,
    val school_id: String,
    val staff_ids: String,
    val student_ids: String,
    val subject: String,
    val teacher_id: String,
    val attachment_type: String,
    val time: String
)