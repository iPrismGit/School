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
        const val AUTH_TOKEN = "auth_token"
        const val ACADEMIC_YEAR_ID = "academic_year_id"
        const val ACADEMIC_YEAR = "academic_year"
        const val SCHOOL_ID = "school_id"
        const val SCHOOL_NAME = "school_name"
        const val IMAGE = "image"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val MIDDLE_NAME = "middle_name"
        const val STUDENT_ID = "id"
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