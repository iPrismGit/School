package com.iprism.school.model.Response

data class DayCareViewListResponse(
    val message: String,
    val response: ResponseDayCareList,
    val status: Boolean
)

data class ResponseDayCareList(
    val daycare: List<Daycare>
)

data class Daycare(
    val id: String,
    val image: String,
    val name: String,
    val type: String
)