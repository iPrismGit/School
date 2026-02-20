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
        const val ACADEMIC_YEAR_ID = "academic_year_id"
        const val ACADEMIC_YEAR = "academic_year"
        const val SCHOOL_ID = "school_id"
        const val SCHOOL_NAME = "school_name"
        const val IMAGE = "image"
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

    fun storeNewUserDetails(
        id: String?,
        first_name: String?,
        middle_name: String?,
        last_name: String?,
        school_id: String?,
        mobile: String?,
        schoolName: String?,
        image: String?
    ) {
        editor.putString(ID, id)
        editor.putString(FIRST_NAME, first_name)
        editor.putString(MIDDLE_NAME, middle_name)
        editor.putString(LAST_NAME, last_name)
        editor.putString(SCHOOL_ID, school_id)
        editor.putString(MOBILE, mobile)
        editor.putString(SCHOOL_NAME, schoolName)
        editor.putString(IMAGE, image)
        editor.putBoolean(IS_USER_LOGIN, true)
        editor.commit()
    }

    fun storeNewUserAuthToken(auth_token: String?) {
        editor.putString(AUTH_TOKEN, auth_token)
        editor.commit()
    }

    fun storeAcademicYear(academic_year_id: String?, academic_year: String?) {
        editor.putString(ACADEMIC_YEAR_ID, academic_year_id)
        editor.putString(ACADEMIC_YEAR, academic_year)
        editor.commit()
    }

    fun getNewUserDetails(): HashMap<String, String?> {
        val user = HashMap<String, String?>()
        user[ID] = sharedPreferences.getString(ID, null)
        user[AUTH_TOKEN] = sharedPreferences.getString(AUTH_TOKEN, null)
        user[ACADEMIC_YEAR] = sharedPreferences.getString(ACADEMIC_YEAR, null)
        user[ACADEMIC_YEAR_ID] = sharedPreferences.getString(ACADEMIC_YEAR_ID, null)
        user[MOBILE] = sharedPreferences.getString(MOBILE, null)
        user[FIRST_NAME] = sharedPreferences.getString(FIRST_NAME, null)
        user[MIDDLE_NAME] = sharedPreferences.getString(MIDDLE_NAME, null)
        user[LAST_NAME] = sharedPreferences.getString(LAST_NAME, null)
        user[SCHOOL_ID] = sharedPreferences.getString(SCHOOL_ID, null)
        user[SCHOOL_NAME] = sharedPreferences.getString(SCHOOL_NAME, null)
        user[IMAGE] = sharedPreferences.getString(IMAGE, null)
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