package com.iprism.school.model.Response

data class GroupsResponse(
    val message: String,
    val response: ResponseTeacherGroups,
    val status: Boolean
)

data class ResponseTeacherGroups(
    val groups: List<GroupsTeacher>
)

data class GroupsTeacher(
    val attachment: String,
    val created_on: String,
    val delete_status: String,
    val group_admins: String,
    val group_description: String,
    val group_name: String,
    val group_staff: String,
    val group_status: String,
    val group_students: String,
    val id: String,
    val school_id: String,
    val updated_on: String
)