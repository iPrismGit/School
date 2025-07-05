package com.iprism.school.model.Request

data class CalenderUpdateReq(
    val advance_options: String,
    val attachment: String,
    val attachment_type: String,
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
    val time: String,
    val calender_id: String,
)