package com.example.hara_gym.data.repository

import com.example.hara_gym.data.api.ApiService
import com.example.hara_gym.data.model.DietPlanDto
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DietRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getMyDietPlan(): Response<DietPlanDto> {
        return apiService.getMyDietPlan()
    }
}
