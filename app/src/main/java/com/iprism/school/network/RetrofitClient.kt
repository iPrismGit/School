package com.iprism.school.network

import com.iprism.parentapp.network.StaffApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://lpipreschool.com/schoolapp/api/api/"

    val apiService: StaffApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StaffApiService::class.java)
    }
}