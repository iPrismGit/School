package com.iprism.school.model.classteachermodel

import com.google.gson.annotations.SerializedName

data class ClassTeacherApiResponse(

    val message: String,
    @SerializedName("response")
    val academicYearsResponse: List<AcademicYear>,
    @SerializedName("response")
    val classesResponse: List<Class>,
    @SerializedName("response")
    val sectionsResponse: List<Section>,
    val status: Boolean

)

data class Section(

    val section_id: String,
    val section_name: String

)

data class Class(

    val class_id: String,
    val class_name: String

)

data class AcademicYear(

    val id: String,
    val name: String

)