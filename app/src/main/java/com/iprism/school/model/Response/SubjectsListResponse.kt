package com.iprism.school.model.Response

data class SubjectsListResponse(
    val message: String,
    val response: ResponseList,
    val status: Boolean
)

data class ResponseList(
    val subjects: List<SubjectLsit>
)

data class SubjectLsit(
    val created_on: String,
    val delete_status: String,
    val description: String,
    val id: String,
    val school_id: String,
    val subject_name: String,
    val subject_type: String,
    val updated_on: Any
)