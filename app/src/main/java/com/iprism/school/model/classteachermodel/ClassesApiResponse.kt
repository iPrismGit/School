package com.iprism.school.model.classteachermodel

data class ClassesApiResponse(

    val message: String,
    val response: List<ClassesResponse>,
    val status: Boolean

)

data class ClassesResponse(

    val class_id: String,
    val class_name: String

)