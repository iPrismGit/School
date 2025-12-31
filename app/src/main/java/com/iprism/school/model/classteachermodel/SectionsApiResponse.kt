package com.iprism.school.model.classteachermodel

data class SectionsApiResponse(

    val message: String,
    val response: List<SectionsResponse>,
    val status: Boolean

)

data class SectionsResponse(

    val section_id: String,
    val section_name: String

)