package com.iprism.school.model.Response

data class ClassListResponse(
    val message: String,
    val response: ResponseClassesee,
    val status: Boolean
)

data class ResponseClassesee(
    val classes: List<ClasseListrr>
)

data class ClasseListrr(
    val class_name: String,
    val class_section: String,
    val class_session: String,
    val id: String,
    val student_count: String,
    val total_students: String
)