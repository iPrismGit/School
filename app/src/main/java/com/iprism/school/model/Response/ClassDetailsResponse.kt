package com.iprism.school.model.Response

data class ClassDetailsResponse(
    val message: String,
    val response: ResponseCalss,
    val status: Boolean
)

data class ResponseCalss(
    val subjects: List<ClassDetails>
)

data class ClassDetails(
    val class_name: String,
    val class_section: String,
    val class_session: String,
    val created_on: String,
    val delete_status: String,
    val id: String,
    val school_id: String,
    val session_id: String,
    val status: String,
    val teacher_ids: String,
    val teacher_names: String,
    val updated_on: String
)