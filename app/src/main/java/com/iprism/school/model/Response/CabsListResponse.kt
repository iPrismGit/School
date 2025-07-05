package com.iprism.school.model.Response

data class CabsListResponse(
    val message: String,
    val response: ResponseCabsList,
    val status: Boolean
)

data class ResponseCabsList(
    val cabs: List<CabList>
)

data class CabList(
    val cab_name: String,
    val driver_name: String,
    val driver_no: String,
    val id: String,
    val vehicle_no: String
)