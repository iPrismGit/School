package com.iprism.school.model.timetable

import com.google.gson.annotations.SerializedName

data class TimeTableRequest(

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("branch_id")
	val branchId: String,

	@field:SerializedName("student_id")
	val studentId: String,

	@field:SerializedName("page")
	val page: String
)
