package com.iprism.school.model.fees

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


data class FeesApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
) : java.io.Serializable

data class FeeTypesItem(

	@field:SerializedName("total_fee")
	val totalFee: String,

	@field:SerializedName("fee_id")
	val feeId: String,

	@field:SerializedName("fee_type_id")
	val feeTypeId: Int,

	@field:SerializedName("fee_configuration_id")
	val feeConfigurationId: Int,

	@field:SerializedName("category")
	val category: String
) : java.io.Serializable

data class StudentDetails(

	@field:SerializedName("academic_year")
	val academicYear: Int,

	@field:SerializedName("section_id")
	val sectionId: Int,

	@field:SerializedName("class_id")
	val classId: Int,

	@field:SerializedName("last_name")
	val lastName: String,

	@field:SerializedName("middle_name")
	val middleName: String,

	@field:SerializedName("first_name")
	val firstName: String
) : java.io.Serializable

data class Response(

	@field:SerializedName("receipt_number")
	val receiptNumber: Int,

	@field:SerializedName("student_details")
	val studentDetails: StudentDetails,

	@field:SerializedName("total_amount")
	val totalAmount: Int,

	@field:SerializedName("last_receipt_number")
	val lastReceiptNumber: Int,

	@field:SerializedName("paid_amount")
	val paidAmount: Int,

	@field:SerializedName("fee_types")
	val feeTypes: List<FeeTypesItem>,

	@field:SerializedName("configuration_id")
	val configurationId: Int,

	@field:SerializedName("pending_amount")
	val pendingAmount: Int
) : java.io.Serializable
