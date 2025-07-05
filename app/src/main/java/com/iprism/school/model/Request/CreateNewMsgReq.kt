package com.iprism.school.model.Request

data class CreateNewMsgReq(
    val attachment_type: String,
    val attachments: String,
    val class_ids: String,
    val disable_replies: String,
    val group_ids: String,
    val message: String,
    val message_type: String,
    val schedule_date: String,
    val schedule_time: String,
    val school_id: String,
    val signature: String,
    val staff_ids: String,
    val student_ids: String,
    val subject: String,
    val teacher_id: String,
    val auth_token: String
)