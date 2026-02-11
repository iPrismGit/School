package com.iprism.school.model.studentsmodel

import com.google.gson.annotations.SerializedName
import com.iprism.school.model.classteachermodel.Pagination

data class StudentsApiResponse(

    val message: String,
    val response: StudentsResponse,
    val status: Boolean

)

data class StudentsResponse(

    val students: List<Student>,
    val pagination: Pagination

)

data class Student(

    val academic_year: String,
    val child_image: String,
    val class_name: String,
    val class_id: String,
    val first_name: String,
    val id: String,
    val last_name: String,
    val middle_name: String,
    val primary_mobile: String,
    val section_name: String,
    val section_id: String,
    var isSelected: Boolean = false

)