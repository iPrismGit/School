package com.iprism.school.model.studentsmodel

data class StudentsApiRequest(

    val academic_year: String,
    val branch_id: String,
    val class_id: String,
    val page: Int,
    val section_id: String,
    val user_id: String

)