package com.iprism.school.model.Response

data class SubjectTeacherListResponse(
    val message: String,
    val response: ResponseListttt,
    val status: Boolean
)

data class ResponseListttt(
    val subjects: List<SubjectTcharList>
)

data class SubjectTcharList(
    val id: String,
    val subject_name: String,
    val teacher_ids: String,
    val teachers: String
)