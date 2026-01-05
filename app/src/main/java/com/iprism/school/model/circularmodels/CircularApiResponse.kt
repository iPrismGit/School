package com.iprism.school.model.circularmodels

import com.iprism.school.model.classteachermodel.Pagination

data class CircularApiResponse(

    val message: String,
    val response: CircularResponse,
    val status: Boolean

)

data class CircularResponse(

    val circulars: List<Circular>,
    val pagination: Pagination

)

data class Circular(
    val created_on: String,
    val description: String,
    val id: String,
    val image: String,
    val title: String
)