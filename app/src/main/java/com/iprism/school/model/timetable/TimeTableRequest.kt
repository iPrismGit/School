package com.iprism.school.model.timetable

import com.google.gson.annotations.SerializedName

data class TimeTableRequest(

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("branch_id")
	val branchId: String,

	@field:SerializedName("class_id")
	val classId: String,

	@field:SerializedName("section_id")
	val sectionId: String,

	@field:SerializedName("page")
	val page: String
)
