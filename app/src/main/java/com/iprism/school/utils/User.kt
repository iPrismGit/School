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
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val MIDDLE_NAME = "middle_name"
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
        school_id: String?,
        authToken: String?,
        token: String?,
        mobile: String?,
        employee_id: String?,
        employee_name: String?,
        employee_email: String?,
        employee_dob: String?,
        employee_gender: String?,
        employee_image: String?,
        employee_designation: String?,
        employee_class: String?,
        employee_department: String?,
        employee_use_designation: String?,
        deleteStatus: String?,
        createdOn: String?,
        updated_on: String?
    ) {
        editor.putString(ID, id)
        editor.putString(SCHOOL_ID, school_id)
        editor.putString(AUTH_TOKEN, authToken)
        editor.putString(TOKEN, token)
        editor.putString(MOBILE, mobile)
        editor.putString(EMP_ID, employee_id)
        editor.putString(EMP_NAME, employee_name)
        editor.putString(EMP_EMAIL, employee_email)
        editor.putString(EMP_DOB, employee_dob)
        editor.putString(EMP_GENDER, employee_gender)
        editor.putString(EMP_IMG, employee_image)
        editor.putString(EMP_DESIGNATION, employee_designation)
        editor.putString(EMP_CLASS, employee_class)
        editor.putString(EMP_DEPARTMENT, employee_department)
        editor.putString(EMP_USE_DESIGNATION, employee_use_designation)

        editor.putString(DELETE_STATUS, deleteStatus)
        editor.putString(CREATED_ON, createdOn)
        editor.putString(UPDATED_ON, updated_on)
        editor.putBoolean(IS_USER_LOGIN, true)
        editor.commit()
    }

    fun storeNewUserDetails(
        id: String?,
        first_name: String?,
        middle_name: String?,
        last_name: String?,
        school_id: String?,
        mobile: String?
    ) {
        editor.putString(ID, id)
        editor.putString(FIRST_NAME, first_name)
        editor.putString(MIDDLE_NAME, middle_name)
        editor.putString(LAST_NAME, last_name)
        editor.putString(SCHOOL_ID, school_id)
        editor.putString(MOBILE, mobile)
        editor.putBoolean(IS_USER_LOGIN, true)
        editor.commit()
    }

    fun storeNewUserAuthToken(auth_token: String?) {
        editor.putString(AUTH_TOKEN, auth_token)
        editor.commit()
    }

    fun getUserDetails(): HashMap<String, String?> {
        val user = HashMap<String, String?>()
        user[ID] = sharedPreferences.getString(ID, null)
        user[AUTH_TOKEN] = sharedPreferences.getString(AUTH_TOKEN, null)
        user[TOKEN] = sharedPreferences.getString(TOKEN, null)
        user[MOBILE] = sharedPreferences.getString(MOBILE, null)

        user[EMP_ID] = sharedPreferences.getString(EMP_ID, null)
        user[EMP_NAME] = sharedPreferences.getString(EMP_NAME, null)
        user[EMP_EMAIL] = sharedPreferences.getString(EMP_EMAIL, null)
        user[EMP_DOB] = sharedPreferences.getString(EMP_DOB, null)
        user[EMP_GENDER] = sharedPreferences.getString(EMP_GENDER, null)
        user[EMP_IMG] = sharedPreferences.getString(EMP_IMG, null)
        user[EMP_DESIGNATION] = sharedPreferences.getString(EMP_DESIGNATION, null)
        user[EMP_CLASS] = sharedPreferences.getString(EMP_CLASS, null)
        user[EMP_DEPARTMENT] = sharedPreferences.getString(EMP_DEPARTMENT, null)
        user[EMP_USE_DESIGNATION] = sharedPreferences.getString(EMP_USE_DESIGNATION, null)
        user[DELETE_STATUS] = sharedPreferences.getString(DELETE_STATUS, null)
        user[CREATED_ON] = sharedPreferences.getString(CREATED_ON, null)
        user[UPDATED_ON] = sharedPreferences.getString(UPDATED_ON, null)

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
        user[FATHER_OFFICE_DESIGNATION] =
            sharedPreferences.getString(FATHER_OFFICE_DESIGNATION, null)
        user[FATHER_OFFICE_ADDRESS] = sharedPreferences.getString(FATHER_OFFICE_ADDRESS, null)
        user[FATHER_ANNUAL_INCOME] = sharedPreferences.getString(FATHER_ANNUAL_INCOME, null)
        user[MOTHER_OCCUPATION] = sharedPreferences.getString(MOTHER_OCCUPATION, null)
        user[MOTHER_OFFICE_DESIGNATION] =
            sharedPreferences.getString(MOTHER_OFFICE_DESIGNATION, null)
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

    fun getNewUserDetails(): HashMap<String, String?> {
        val user = HashMap<String, String?>()
        user[ID] = sharedPreferences.getString(ID, null)
        user[AUTH_TOKEN] = sharedPreferences.getString(AUTH_TOKEN, null)
        user[MOBILE] = sharedPreferences.getString(MOBILE, null)
        user[FIRST_NAME] = sharedPreferences.getString(FIRST_NAME, null)
        user[MIDDLE_NAME] = sharedPreferences.getString(MIDDLE_NAME, null)
        user[LAST_NAME] = sharedPreferences.getString(LAST_NAME, null)
        user[SCHOOL_ID] = sharedPreferences.getString(SCHOOL_ID, null)
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