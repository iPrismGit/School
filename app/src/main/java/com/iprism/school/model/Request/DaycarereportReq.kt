package com.iprism.school.model.Request

data class DaycarereportReq(
    val attachment: String,
    val auth_token: String,
    val date: String,
    val end_time: String,
    val group_id: String,
    val meal_name: String,
    val notify_parent: String,
    val quantity: String,
    val remarks: String,
    val report_type: String,
    val school_id: String,
    val start_time: String,
    val student_ids: String,
    val teacher_id: String,
    val edit: String
)