package com.iprism.school.model.Response

data class ConsentsListResponse(
    val message: String,
    val response: ResponseConsentsList,
    val status: Boolean
)

data class ResponseConsentsList(
    val consent_details: List<ConsentListDetail>
)

data class ConsentListDetail(
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
    val teacher_id: String,
    val time: String,
    val title: String,
    val type: String,
    val updated_on: Any
)