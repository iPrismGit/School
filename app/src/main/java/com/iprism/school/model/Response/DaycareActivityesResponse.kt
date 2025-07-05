package com.iprism.school.model.Response

data class DaycareActivityesResponse(
    val message: String,
    val response: ResponseDaycareActivity,
    val status: Boolean
)

data class ResponseDaycareActivity(
    val activities: List<ActivityList>
)

data class ActivityList(
    val activity_icon: String,
    val activity_name: String,
    val created_on: String,
    val date: String,
    val delete_status: String,
    val description: String,
    val end_time: String,
    val id: String,
    val school_id: String,
    val start_time: String,
    val updated_on: String
)