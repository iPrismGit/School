package com.iprism.school.model.Response

data class GroupsListResponse(
    val message: String,
    val response: ResponseGroups,
    val status: Boolean
)

data class ResponseGroups(
    val groups: List<GroupList>
)

data class GroupList(
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
    val school_members: String,
    val student_count: String,
    val student_names: String,
    val updated_on: String
)