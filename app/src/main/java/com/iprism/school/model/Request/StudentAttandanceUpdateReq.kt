package com.iprism.school.model.Request

data class StudentAttandanceUpdateReq(
    val attendance: String,
    val auth_token: String,
    val mark_as: String,
    val qrcode_id: String,
    val send_notification: String,
    val teacher_id: String
)