package com.iprism.school.model.classteachermodel

data class ClassTeacherApiRequest(

    val class_id: String,
    val user_id: String,
    val branch_id: String,
    val academic_year: String,
    val view_type: String

)