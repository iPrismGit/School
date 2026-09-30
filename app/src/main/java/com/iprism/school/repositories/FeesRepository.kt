package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.fees.FeesApiResponse
import com.iprism.school.model.fees.FeesRequest
import com.iprism.school.model.insertfee.InsertFeeApiResponse
import com.iprism.school.model.insertfee.InsertFeeRequest
import com.iprism.school.network.SchoolApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FeesRepository(private var context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun fetchFees(request: FeesRequest): FeesApiResponse =
        withContext(Dispatchers.IO) {
            var response = apiService.fetchFees(request)
            if (response.message.equals("Invalid or expired token", true)) {
                val refreshed = authRepository.refreshToken()
                if (refreshed) {
                    response = apiService.fetchFees(request)
                }
            }
            response
        }

    suspend fun insertFee(request: InsertFeeRequest): InsertFeeApiResponse =
        withContext(Dispatchers.IO) {
            var response = apiService.insertFee(request)
            if (response.message.equals("Invalid or expired token", true)) {
                val refreshed = authRepository.refreshToken()
                if (refreshed) {
                    response = apiService.insertFee(request)
                }
            }
            response
        }
}
