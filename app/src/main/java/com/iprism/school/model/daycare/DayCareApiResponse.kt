package com.iprism.school.model.daycare

import com.iprism.school.model.classteachermodel.Pagination
import kotlinx.serialization.SerialName

data class DayCareApiResponse(

    val message: String,
    val response: DayCareResponse,
    val status: Boolean

)

data class DayCareResponse(

    val categories: List<Category>,
    val students: List<Student>,
    val pagination: Pagination,
    val attendance_status: String

)

data class Category(

    val cat_id: String,
    val name: String

)

data class Student(

    val academic_year: String,
    val attendance_id: String,
    val attendance_status: String,
    val cat_id: String,
    val child_image: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val middle_name: String

)