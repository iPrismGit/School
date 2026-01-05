package com.iprism.school.model.eventsmodel

import com.iprism.school.model.classteachermodel.Pagination

data class EventsApiResponse(

    val message: String,
    val response: EventsResponse,
    val status: Boolean

)

data class EventsResponse(

    val events: List<Event>,
    val pagination: Pagination

)

data class Event(

    val cat_id: String,
    val category: String,
    val description: String,
    val end_date: String,
    val hour: String,
    val id: String,
    val image: String,
    val minute: String,
    val reminder_date: String,
    val start_date: String,
    val time_zone: String,
    val title: String

)