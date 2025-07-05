package com.iprism.school.model.Response

data class GroupDetailsResponse(
    val message: String,
    val response: ResponseGroupDetails,
    val status: Boolean
)

data class ResponseGroupDetails(
    val groups: GroupsDetails
)

data class GroupsDetails(
    val attachment: String,
    val created_on: String,
    val delete_status: String,
    val group_admins: String,
    val group_admins_names: String,
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
    val students_details: List<StudentsDetailList>,
    val updated_on: Any
)

data class StudentsDetailList(
    val admission_id: String,
    val id: String,
    val student_image: String,
    val student_name: String
)