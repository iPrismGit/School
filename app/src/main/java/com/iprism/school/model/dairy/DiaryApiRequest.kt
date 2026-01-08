package com.iprism.school.model.dairy

data class DiaryApiRequest(

    val academic_year: String,
    val branch_id: String,
    val class_id: String,
    val date: String,
    val details: String,
    val id: String,
    val image: String,
    val page: Int,
    val section_id: String,
    val student_type: String,
    val students: List<Student>,
    val type: String,
    val user_id: String,
    val view_type: String

)

data class Student(
    val id: Int
)