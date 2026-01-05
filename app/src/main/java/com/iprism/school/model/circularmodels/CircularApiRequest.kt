package com.iprism.school.model.circularmodels

data class CircularApiRequest(

    val academic_year: String,
    val branch_id: String,
    val page: Int,
    val user_id: String

)