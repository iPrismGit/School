package com.iprism.school.model.fees

import com.google.gson.annotations.SerializedName

data class FeesRequest(

	@field:SerializedName("section_id")
	val sectionId: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("branch_id")
	val branchId: String,

	@field:SerializedName("class_id")
	val classId: String,

	@field:SerializedName("student_id")
	val studentId: String
)
