package com.iprism.school.model.Response

data class SingleMsgDetailsResponse(
    val message: String,
    val response: ResponseSingleMsg,
    val status: Boolean
)

data class ResponseSingleMsg(
    val replys: List<Any>,
    val sent_messages: List<SentMessage>
)

data class SentMessage(
    val attachment_type: String,
    val attachments: String,
    val created_on: String,
    val date: String,
    val delete_status: String,
    val disable_replies: String,
    val from: String,
    val id: String,
    val message: String,
    val message_status: String,
    val message_type: String,
    val schedule_date: String,
    val schedule_time: String,
    val school_id: String,
    val sent_from: String,
    val signature: String,
    val staff_names: String,
    val student_names: String,
    val subject: String,
    val teacher_id: String,
    val to_class: String,
    val to_group: String,
    val to_staff: String,
    val to_student: String,
    val updated_on: Any
)