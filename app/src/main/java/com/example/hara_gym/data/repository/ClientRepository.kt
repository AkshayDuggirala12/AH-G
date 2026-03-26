package com.example.hara_gym.data.repository

import com.example.hara_gym.data.api.ApiService
import com.example.hara_gym.data.model.MyPlansResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getMyPlans(): Response<MyPlansResponse> {
        return apiService.getMyPlans()
    }
}
