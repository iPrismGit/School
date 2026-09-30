package com.iprism.school.model.fees

import com.google.gson.annotations.SerializedName

data class FeesRequest(

	@field:SerializedName("section_id")
	val sectionId: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("branch_id")
	val branchId: Int,

	@field:SerializedName("class_id")
	val classId: Int,

	@field:SerializedName("student_id")
	val studentId: Int
)
