package com.iprism.school.model.eventsmodel

data class EventsApiRequest(

    val branch_id: String,
    val month: Int,
    val page: Int,
    val user_id: String,
    val year: Int,
    val view_type : String
)