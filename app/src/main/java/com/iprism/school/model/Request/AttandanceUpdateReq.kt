package com.iprism.school.model.Request

data class AttandanceUpdateReq(
    val absent_students: String,
    val auth_token: String,
    val class_id: String,
    val date: String,
    val present_students: String,
    val school_id: String,
    val send_notification: String,
    val teacher_id: String,
    val total_absent: String,
    val total_present: String
)