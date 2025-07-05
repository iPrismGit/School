package com.iprism.school.model.Response

data class ActivityIconsResponse(
    val message: String,
    val response: ResponseActivityIcons,
    val status: Boolean
)

data class ResponseActivityIcons(
    val activities: List<ActivityIconsList>
)

data class ActivityIconsList(
    val activity_icon: String,
    val created_on: String,
    val id: String,
    val icon_name: String,
    val school_id: String
)