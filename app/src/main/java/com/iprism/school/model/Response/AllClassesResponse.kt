package com.iprism.school.model.Response

data class AllClassesResponse(
    val message: String,
    val response: ResponseAllclass,
    val status: Boolean
)

data class ResponseAllclass(
    val classes: List<ClasseAllList>
)

data class ClasseAllList(
    val class_name: String,
    val class_section: String,
    val class_session: String,
    val id: String,
    val student_count: String,
    val total_students: String
)