package com.iprism.school.model.classteachermodel
 data class ClassTeacherApiResponse(

    val message: String,
    val response: List<AcademicYearResponse>,
    val status: Boolean
)

data class AcademicYearResponse(

    val id: String,
    val name: String

)