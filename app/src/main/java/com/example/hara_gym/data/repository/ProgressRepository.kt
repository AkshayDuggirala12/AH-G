package com.example.hara_gym.data.repository

import com.example.hara_gym.data.api.ApiService
import com.example.hara_gym.data.model.ToggleExerciseRequest
import com.example.hara_gym.data.model.WeeklyProgressItemDto
import com.example.hara_gym.data.model.WorkoutDayProgressDto
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getWeeklyProgress(progressDate: String? = null): Response<List<WeeklyProgressItemDto>> {
        return apiService.getWeeklyProgress(progressDate)
    }

    suspend fun getDayProgress(dayName: String, progressDate: String? = null): Response<WorkoutDayProgressDto> {
        return apiService.getDayProgress(dayName, progressDate)
    }

    suspend fun toggleExerciseProgress(request: ToggleExerciseRequest): Response<WorkoutDayProgressDto> {
        return apiService.toggleExercise(request)
    }
}
