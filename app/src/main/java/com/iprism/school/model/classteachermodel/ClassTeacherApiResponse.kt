package com.iprism.school.model.classteachermodel
  data class ClassTeacherApiResponse(

    val message: String,
    val response: ClassTeacherResponse,
    val status: Boolean

)

data class ClassTeacherResponse(

    val classes: List<Class>,
    val sections: List<Section>,
    val id: String,
    val name: String

)

data class Class(

    val class_id: String,
    val class_name: String

)

data class Section(

    val section_id: String,
    val section_name: String

)