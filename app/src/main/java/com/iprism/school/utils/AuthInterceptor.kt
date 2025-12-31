package com.iprism.school.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    @Volatile
    var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val builder = originalRequest.newBuilder()
            .addHeader("Accept", "application/json")

        token?.let {
            val authHeader = "Bearer $it"
            builder.addHeader("Authorization", authHeader)

            Log.d("AuthInterceptor", "Authorization Header: $authHeader")
        } ?: run {
            Log.w("AuthInterceptor", "Authorization token is NULL")
        }

        return chain.proceed(builder.build())
    }
}
