package com.iprism.school.model.eventsmodel

data class EventsApiRequest(

    val academic_year: String,
    val branch_id: String,
    val class_id: String,
    val month: Int,
    val page: Int,
    val section_id: String,
    val user_id: String,
    val year: Int
)