package com.iprism.school.utils

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val user = User(context)
        val token = user.getNewUserDetails()[User.AUTH_TOKEN]

        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .apply {
                if (!token.isNullOrEmpty()) addHeader("Authorization", "Bearer $token")
            }
            .build()

        return chain.proceed(request)
    }

}
