package com.iprism.school.model.Response

data class AllCabsResponse(
    val message: String,
    val response: Responselissyyy,
    val status: Boolean
)

data class Responselissyyy(
    val cabs: List<CabListTs>
)

data class CabListTs(
    val cab_name: String,
    val driver_name: String,
    val driver_no: String,
    val id: String,
    val vehicle_no: String
)