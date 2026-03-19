package com.example.hara_gym.data.api

import com.example.hara_gym.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<User>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("workouts/weekly")
    suspend fun getWeeklyWorkouts(@Header("Authorization") token: String): Response<List<Workout>>

    @GET("workouts/days/{day_name}")
    suspend fun getWorkoutByDay(
        @Header("Authorization") token: String,
        @Path("day_name") dayName: String
    ): Response<Workout>
}
