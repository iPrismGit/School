package com.iprism.school.model.Response

data class ClassResponse(
    val message: String,
    val response: ResponseClasses,
    val status: Boolean
)

data class ResponseClasses(
    val classes: List<ClasseList>
)

data class ClasseList(
    val class_name: String,
    val class_section: String,
    val created_on: String,
    val delete_status: String,
    val id: String,
    val school_id: String,
    val session_id: String,
    val status: String,
    val updated_on: String
)