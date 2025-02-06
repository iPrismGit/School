package com.iprism.school.network

import com.iprism.parentapp.network.StaffApiService
import com.iprism.school.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StaffApi {

    fun createParentApiService(): StaffApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(StaffApiService::class.java)
    }

}