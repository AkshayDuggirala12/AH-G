package com.example.hara_gym.data.api

import com.example.hara_gym.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<UserDto>

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @GET("clients/my-plans")
    suspend fun getMyPlans(): Response<MyPlansResponse>

    @GET("workouts/my-plan")
    suspend fun getMyWorkoutPlan(): Response<WorkoutPlanDto>

    @GET("diets/my-plan")
    suspend fun getMyDietPlan(): Response<DietPlanDto>

    @GET("progress/weekly")
    suspend fun getWeeklyProgress(
        @Query("progress_date") progressDate: String? = null
    ): Response<List<WeeklyProgressItemDto>>

    @GET("progress/days/{dayName}")
    suspend fun getDayProgress(
        @Path("dayName") dayName: String,
        @Query("progress_date") progressDate: String? = null
    ): Response<WorkoutDayProgressDto>

    @POST("progress/exercise/toggle")
    suspend fun toggleExercise(
        @Body body: ToggleExerciseRequest
    ): Response<WorkoutDayProgressDto>
}
