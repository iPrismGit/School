package com.iprism.school.model.Response

data class InboxSingleMsgResponse(
    val message: String,
    val response: ResponseInboxSingle,
    val status: Boolean
)

data class ResponseInboxSingle(
    val inbox_message: List<InboxMessageInboxSingle>
)

data class InboxMessageInboxSingle(
    val archived_message: String,
    val attachment_type: String,
    val attachments: String,
    val class_names: String,
    val created_on: String,
    val date: String,
    val delete_status: String,
    val disable_replies: String,
    val from: String,
    val group_names: String,
    val id: String,
    val images: String,
    val profile_image: String,
    val message: String,
    val message_status: String,
    val message_type: String,
    val name: String,
    val read_message: String,
    val schedule_date: String,
    val schedule_time: String,
    val school_id: String,
    val sent_from: String,
    val signature: String,
    val staff_names: String,
    val starred_message: String,
    val student_names: String,
    val subject: String,
    val teacher_id: String,
    val to_class: String,
    val to_group: String,
    val to_staff: String,
    val to_student: String,
    val updated_on: Any
)