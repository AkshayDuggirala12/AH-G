package com.example.hara_gym.data.model

import com.google.gson.annotations.SerializedName

data class WeeklyProgressItemDto(
    @SerializedName("day_name") val dayName: String,
    val title: String,
    @SerializedName("progress_date") val progressDate: String,
    @SerializedName("completed_exercises") val completedExercises: Int,
    @SerializedName("total_exercises") val totalExercises: Int,
    @SerializedName("remaining_exercises") val remainingExercises: Int,
    val percentage: Double,
    @SerializedName("is_day_completed") val isDayCompleted: Boolean
)

data class ExerciseProgressStatusDto(
    @SerializedName("exercise_id") val exerciseId: Int,
    @SerializedName("exercise_name") val exerciseName: String,
    @SerializedName("is_completed") val isCompleted: Boolean,
    @SerializedName("completed_at") val completedAt: String?
)

data class WorkoutDayProgressDto(
    @SerializedName("day_name") val dayName: String,
    val title: String,
    @SerializedName("progress_date") val progressDate: String,
    @SerializedName("completed_exercises") val completedExercises: Int,
    @SerializedName("total_exercises") val totalExercises: Int,
    @SerializedName("remaining_exercises") val remainingExercises: Int,
    val percentage: Double,
    @SerializedName("is_day_completed") val isDayCompleted: Boolean,
    val exercises: List<ExerciseProgressStatusDto>
)

data class ToggleExerciseRequest(
    @SerializedName("exercise_id") val exerciseId: Int,
    @SerializedName("is_completed") val isCompleted: Boolean,
    @SerializedName("progress_date") val progressDate: String? = null
)

data class ErrorResponse(
    val detail: String
)
