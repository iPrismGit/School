package com.iprism.school.model.daycare

data class DayCareStatusApiResponse(

    val message: String,
    val response: DayCareStatusResponse,
    val status: Boolean

)

data class DayCareStatusResponse(

    val status: String

)