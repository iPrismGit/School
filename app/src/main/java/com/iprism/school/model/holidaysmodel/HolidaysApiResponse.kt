package com.iprism.school.model.holidaysmodel

data class HolidaysApiResponse(

    val message: String,
    val response: HolidaysResponse,
    val status: Boolean

)

data class HolidaysResponse(

    val holidays: List<Holiday>

)

data class Holiday(

    val date: String,
    val day: String,
    val status: String,
    val title: String

)