package com.iprism.school.utils

import android.content.Context
import android.content.SharedPreferences

class User(var context: Context) {

    var sharedPreferences: SharedPreferences
    var editor: SharedPreferences.Editor
    var PRIVATE_MODE = 0

    init {
        sharedPreferences = context.getSharedPreferences("PARENT APP", PRIVATE_MODE)
        editor = sharedPreferences.edit()
    }

    companion object {
        const val ID = "id"
        const val MOBILE = "mobile"
        const val TOKEN = "token"
        const val AUTH_TOKEN = "auth_token"
        const val SCHOOL_ID = "school_id"
        const val EMP_ID = "employee_id"
        const val EMP_NAME = "employee_name"
        const val EMP_EMAIL = "employee_email"
        const val EMP_DOB = "employee_dob"
        const val EMP_GENDER = "employee_gender"
        const val EMP_IMG = "employee_image"
        const val EMP_DESIGNATION = "employee_designation"
        const val EMP_DEPARTMENT = "employee_department"
        const val EMP_USE_DESIGNATION = "employee_use_designation"
        const val EMP_CLASS = "employee_class"
        const val QRCODE = "qrcode"
        const val QRCODE_ID = "qrcode_id"
        const val STATUS = "status"

        const val DELETE_STATUS = "delete_status"
        const val CREATED_ON = "created_on"
        const val UPDATED_ON = "updated_on"
        const val STUDENT_ID = "id"
        const val STUDENT_SCHOOL_ID = "school_id"
        const val STUDENT_CLASS_ID = "class_id"
        const val STUDENT_SESSION_ID = "session_id"
        const val STUDENT_ADMISSION_ID = "admission_id"
        const val STUDENT_JOINING_DATE = "joining_date"
        const val STUDENT_NAME = "student_name"
        const val STUDENT_DOB = "student_dob"
        const val STUDENT_GENDER = "student_gender"
        const val STUDENT_B_G = "student_blood_group"
        const val STUDENT_IMAGE = "student_image"
        const val FATHER_NAME = "father_name"
        const val FATHER_MOBILE = "father_mobile"
        const val FATHER_EMAIL = "father_email"
        const val FATHER_IMAGE = "father_image"
        const val MOTHER_NAME = "mother_name"
        const val MOTHER_MOBILE = "mother_mobile"
        const val MOTHER_EMAIL = "mother_email"
        const val MOTHER_IMAGE = "mother_image"
        const val GUARDIAN_NAME = "guardian_name"
        const val GUARDIAN_MOBILE = "guardian_mobile"
        const val GUARDIAN_EMAIL = "guardian_email"
        const val GUARDIAN_IMAGE = "guardian_image"
        const val ADDRESS = "address"
        const val CITY = "city"
        const val PINCODE = "pincode"
        const val FATHER_OCCUPATION = "father_occupation"
        const val FATHER_OFFICE_DESIGNATION = "father_office_designation"
        const val FATHER_OFFICE_ADDRESS = "father_office_address"
        const val FATHER_ANNUAL_INCOME = "father_annual_income"
        const val MOTHER_OCCUPATION = "mother_occupation"
        const val MOTHER_OFFICE_DESIGNATION = "mother_office_designation"
        const val MOTHER_OFFICE_ADDRESS = "mother_office_address"
        const val MOTHER_ANNUAL_INCOME = "mother_annual_income"
        const val STUDENT_DELETE_STATUS = "delete_status"
        const val STUDENT_CREATED_ON = "created_on"
        const val STUDENT_UPDATED_ON = "updated_on"
        const val IS_USER_LOGIN = "isUserLogin"
    }

    fun storeUserDetails(
        id: String?,
        school_id : String?,
        authToken: String?,
        token : String?,
        mobile : String?,
        employee_id: String?,
        employee_name : String?,
        employee_dob: String?,
        employee_gender : String?,
        employee_image : String?,
        employee_designation: String?,
        employee_department : String?,
        employee_use_designation: String?,
        deleteStatus : String?,
        createdOn : String?,
        updated_on : String?) {
        editor.putString(ID, id)
        editor.putString(SCHOOL_ID, school_id)
        editor.putString(AUTH_TOKEN, authToken)
        editor.putString(TOKEN, token)
        editor.putString(MOBILE, mobile)
        editor.putString(EMP_ID, employee_id)
        editor.putString(EMP_NAME, employee_name)
        editor.putString(EMP_DOB, employee_dob)
        editor.putString(EMP_GENDER, employee_gender)
        editor.putString(EMP_IMG, employee_image)
        editor.putString(EMP_DESIGNATION, employee_designation)
        editor.putString(EMP_DEPARTMENT, employee_department)
        editor.putString(EMP_USE_DESIGNATION, employee_use_designation)

        editor.putString(DELETE_STATUS, deleteStatus)
        editor.putString(CREATED_ON, createdOn)
        editor.putString(UPDATED_ON, updated_on)
        editor.putBoolean(IS_USER_LOGIN, true)
        editor.commit()
    }

    fun storeStudentDetails(id: String?, school_id : String?, class_id : String?, sessionId : String?,
                            admission_id : String?, joining_date : String?, student_name : String?, student_dob : String?,
                            student_gender : String?, student_blood_group : String?, student_image : String?, father_name : String?,
                            father_mobile : String?, father_email : String?, father_image : String?, mother_name : String?,
                            mother_mobile : String?, mother_email : String?, mother_image : String?, guardian_name : String?,
                            guardian_mobile : String?, guardian_email : String?, guardian_image : String?, address : String?,
                            city : String?, pincode : String?, father_occupation : String?, father_office_designation : String?,
                            father_office_address : String?, father_annual_income : String?, mother_occupation : String?, mother_office_designation : String?,
                            mother_office_address : String?, mother_annual_income : String?, qrcode : String?, qrcode_id : String?,
                            status : String?, delete_status : String?, created_on : String?, updated_on : String?) {
        editor.putString(STUDENT_ID, id)
        editor.putString(STUDENT_SCHOOL_ID, school_id)
        editor.putString(STUDENT_CLASS_ID, class_id)
        editor.putString(STUDENT_SESSION_ID, sessionId)
        editor.putString(STUDENT_ADMISSION_ID, admission_id)
        editor.putString(STUDENT_JOINING_DATE, joining_date)
        editor.putString(STUDENT_NAME, student_name)
        editor.putString(STUDENT_DOB, student_dob)
        editor.putString(STUDENT_GENDER, student_gender)
        editor.putString(STUDENT_B_G, student_blood_group)
        editor.putString(STUDENT_IMAGE, student_image)
        editor.putString(FATHER_NAME, father_name)
        editor.putString(FATHER_MOBILE, father_mobile)
        editor.putString(FATHER_EMAIL, father_email)
        editor.putString(FATHER_IMAGE, father_image)
        editor.putString(MOTHER_NAME, mother_name)
        editor.putString(MOTHER_MOBILE, mother_mobile)
        editor.putString(MOTHER_EMAIL, mother_email)
        editor.putString(MOTHER_IMAGE, mother_image)
        editor.putString(GUARDIAN_NAME, guardian_name)
        editor.putString(GUARDIAN_MOBILE, guardian_mobile)
        editor.putString(GUARDIAN_EMAIL, guardian_email)
        editor.putString(GUARDIAN_IMAGE, guardian_image)
        editor.putString(ADDRESS, address)
        editor.putString(CITY, city)
        editor.putString(PINCODE, pincode)
        editor.putString(FATHER_OCCUPATION, father_occupation)
        editor.putString(FATHER_OFFICE_DESIGNATION, father_office_designation)
        editor.putString(FATHER_OFFICE_ADDRESS, father_office_address)
        editor.putString(FATHER_ANNUAL_INCOME, father_annual_income)
        editor.putString(MOTHER_OCCUPATION, mother_occupation)
        editor.putString(MOTHER_OFFICE_DESIGNATION, mother_office_designation)
        editor.putString(MOTHER_OFFICE_ADDRESS, mother_office_address)
        editor.putString(MOTHER_ANNUAL_INCOME, mother_annual_income)
        editor.putString(QRCODE, qrcode)
        editor.putString(QRCODE_ID, qrcode_id)
        editor.putString(STATUS, status)
        editor.putString(STUDENT_DELETE_STATUS, delete_status)
        editor.putString(STUDENT_CREATED_ON, created_on)
        editor.putString(STUDENT_UPDATED_ON, updated_on)
        editor.commit()
    }

    fun getUserDetails(): HashMap<String, String?> {
        val user = HashMap<String, String?>()
        user[ID] = sharedPreferences.getString(ID, null)
        user[AUTH_TOKEN] = sharedPreferences.getString(AUTH_TOKEN, null)
        user[MOBILE] = sharedPreferences.getString(MOBILE, null)
        user[TOKEN] = sharedPreferences.getString(TOKEN, null)
        user[DELETE_STATUS] = sharedPreferences.getString(DELETE_STATUS, null)
        user[CREATED_ON] = sharedPreferences.getString(CREATED_ON, null)
        user[STUDENT_ID] = sharedPreferences.getString(STUDENT_ID, null)
        user[STUDENT_SCHOOL_ID] = sharedPreferences.getString(STUDENT_SCHOOL_ID, null)
        user[STUDENT_CLASS_ID] = sharedPreferences.getString(STUDENT_CLASS_ID, null)
        user[STUDENT_SESSION_ID] = sharedPreferences.getString(STUDENT_SESSION_ID, null)
        user[STUDENT_ADMISSION_ID] = sharedPreferences.getString(STUDENT_ADMISSION_ID, null)
        user[STUDENT_JOINING_DATE] = sharedPreferences.getString(STUDENT_JOINING_DATE, null)
        user[STUDENT_NAME] = sharedPreferences.getString(STUDENT_NAME, null)
        user[STUDENT_DOB] = sharedPreferences.getString(STUDENT_DOB, null)
        user[STUDENT_GENDER] = sharedPreferences.getString(STUDENT_GENDER, null)
        user[STUDENT_B_G] = sharedPreferences.getString(STUDENT_B_G, null)
        user[STUDENT_IMAGE] = sharedPreferences.getString(STUDENT_IMAGE, null)
        user[FATHER_NAME] = sharedPreferences.getString(FATHER_NAME, null)
        user[FATHER_MOBILE] = sharedPreferences.getString(FATHER_MOBILE, null)
        user[FATHER_EMAIL] = sharedPreferences.getString(FATHER_EMAIL, null)
        user[FATHER_IMAGE] = sharedPreferences.getString(FATHER_IMAGE, null)
        user[MOTHER_NAME] = sharedPreferences.getString(MOTHER_NAME, null)
        user[MOTHER_MOBILE] = sharedPreferences.getString(MOTHER_MOBILE, null)
        user[MOTHER_EMAIL] = sharedPreferences.getString(MOTHER_EMAIL, null)
        user[MOTHER_IMAGE] = sharedPreferences.getString(MOTHER_IMAGE, null)
        user[GUARDIAN_NAME] = sharedPreferences.getString(GUARDIAN_NAME, null)
        user[GUARDIAN_MOBILE] = sharedPreferences.getString(GUARDIAN_MOBILE, null)
        user[GUARDIAN_EMAIL] = sharedPreferences.getString(GUARDIAN_EMAIL, null)
        user[GUARDIAN_IMAGE] = sharedPreferences.getString(GUARDIAN_IMAGE, null)
        user[ADDRESS] = sharedPreferences.getString(ADDRESS, null)
        user[CITY] = sharedPreferences.getString(CITY, null)
        user[PINCODE] = sharedPreferences.getString(PINCODE, null)
        user[FATHER_OCCUPATION] = sharedPreferences.getString(FATHER_OCCUPATION, null)
        user[FATHER_OFFICE_DESIGNATION] = sharedPreferences.getString(FATHER_OFFICE_DESIGNATION, null)
        user[FATHER_OFFICE_ADDRESS] = sharedPreferences.getString(FATHER_OFFICE_ADDRESS, null)
        user[FATHER_ANNUAL_INCOME] = sharedPreferences.getString(FATHER_ANNUAL_INCOME, null)
        user[MOTHER_OCCUPATION] = sharedPreferences.getString(MOTHER_OCCUPATION, null)
        user[MOTHER_OFFICE_DESIGNATION] = sharedPreferences.getString(MOTHER_OFFICE_DESIGNATION, null)
        user[MOTHER_OFFICE_ADDRESS] = sharedPreferences.getString(MOTHER_OFFICE_ADDRESS, null)
        user[MOTHER_ANNUAL_INCOME] = sharedPreferences.getString(MOTHER_ANNUAL_INCOME, null)
        user[QRCODE] = sharedPreferences.getString(QRCODE, null)
        user[QRCODE_ID] = sharedPreferences.getString(QRCODE_ID, null)
        user[STATUS] = sharedPreferences.getString(STATUS, null)
        user[STUDENT_DELETE_STATUS] = sharedPreferences.getString(STUDENT_DELETE_STATUS, null)
        user[STUDENT_CREATED_ON] = sharedPreferences.getString(STUDENT_CREATED_ON, null)
        user[STUDENT_UPDATED_ON] = sharedPreferences.getString(STUDENT_UPDATED_ON, null)
        return user
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_USER_LOGIN, false)
    }

    fun logoutUser() {
        editor.clear()
        editor.apply()
    }



}