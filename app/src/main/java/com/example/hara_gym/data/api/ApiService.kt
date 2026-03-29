package com.example.hara_gym.data.api

import com.example.hara_gym.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Auth
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<UserDto>

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    // Client Plans & Access
    @GET("clients/my-plans")
    suspend fun getMyPlans(): Response<MyPlansResponse>

    @POST("clients/access-request")
    suspend fun submitAccessRequest(@Body body: ClientAccessRequestBody): Response<ClientAccessRequestDto>

    @GET("clients/access-request/me")
    suspend fun getMyAccessRequests(): Response<List<ClientAccessRequestDto>>

    // Legacy/Individual Plan Access (Keeping for now if needed)
    @GET("workouts/my-plan")
    suspend fun getMyWorkoutPlan(): Response<WorkoutPlanDto>

    @GET("diets/my-plan")
    suspend fun getMyDietPlan(): Response<DietPlanDto>

    // Progress
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
