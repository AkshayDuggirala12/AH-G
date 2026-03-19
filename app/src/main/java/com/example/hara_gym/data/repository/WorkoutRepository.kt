package com.example.hara_gym.data.repository

import com.example.hara_gym.data.api.ApiService
import com.example.hara_gym.data.model.*
import retrofit2.Response

class WorkoutRepository(private val apiService: ApiService) {
    suspend fun register(request: RegisterRequest): Response<User> = apiService.register(request)
    suspend fun login(request: LoginRequest): Response<LoginResponse> = apiService.login(request)
    suspend fun getWeeklyWorkouts(token: String): Response<List<Workout>> = apiService.getWeeklyWorkouts("Bearer $token")
    suspend fun getWorkoutByDay(token: String, dayName: String): Response<Workout> = apiService.getWorkoutByDay("Bearer $token", dayName)
}
