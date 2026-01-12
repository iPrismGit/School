package com.iprism.school.model.daycare

data class DayCareApiResponse(

    val message: String,
    val response: DayCareResponse,
    val status: Boolean

)

data class DayCareResponse(

    val categories: List<Category>,
    val students: List<Student>

)

data class Category(

    val id: String,
    val name: String

)

data class Student(

    val academic_year: String,
    val child_image: String,
    val first_name: String,
    val id: String,
    val last_name: String,
    val middle_name: String,
    val primary_mobile: String,
    val user_id: String

)