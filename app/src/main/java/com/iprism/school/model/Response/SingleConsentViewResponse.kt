package com.iprism.school.model.Response

data class SingleConsentViewResponse(
    val message: String,
    val response: ResponseSingleConsent,
    val status: Boolean
)

data class ResponseSingleConsent(
    val attachments: List<AttachmentSingleConsent>,
    val consent_details: List<ConsentDetailSIngle>
)

data class AttachmentSingleConsent(
    val attachment: String,
    val attachment_type: String,
    val consent_id: String,
    val created_on: Any,
    val id: String,
    val updated_on: Any
)

data class ConsentDetailSIngle(
    val calender_time: String,
    val class_ids: String,
    val class_names: String,
    val consent_image: String,
    val created_date: String,
    val created_on: String,
    val date: String,
    val day: String,
    val delete_status: String,
    val details: String,
    val group_ids: String,
    val id: String,
    val school_id: String,
    val status: String,
    val student_ids: String,
    val student_names: String,
    val group_names: String,
    val teacher_id: String,
    val time: String,
    val title: String,
    val type: String,
    val updated_on: String
)