package com.iprism.school.model.classteachermodel

import com.google.gson.annotations.SerializedName
 data class ClassTeacherApiResponse(

    val message: String,
    val response: List<AcademicYearResponse>,
    val status: Boolean
)

data class AcademicYearResponse(

    val id: String,
    val name: String

)