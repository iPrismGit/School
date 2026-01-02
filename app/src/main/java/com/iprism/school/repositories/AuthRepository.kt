package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.refreshtokenmodel.RefreshTokenApiRequest
import com.iprism.school.network.SchoolApi
import com.iprism.school.utils.User

class AuthRepository(private val context: Context) {

    private val api = SchoolApi.create(context)
    private val user = User(context)

    suspend fun refreshToken(): Boolean {
        return try {
            val userId = user.getUserDetails()[User.ID].orEmpty()
            if (userId.isEmpty()) return false

            val refreshToken = user.getUserDetails()[User.AUTH_TOKEN].orEmpty()
            if (refreshToken.isEmpty()) return false

            val response = api.refreshToken(
                RefreshTokenApiRequest(userId)
            )

            return if (response.status) {
                user.storeNewUserAuthToken(response.response.token)
                true
            } else {
                false
            }

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}
