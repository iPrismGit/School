package com.iprism.school.model.notifications

import com.google.gson.annotations.SerializedName

data class NotificationsRequest(

    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("student_id")
    val studentId: String,

    @field:SerializedName("branch_id")
    val branchId: String,

    @field:SerializedName("page")
    val page: Int,
)
