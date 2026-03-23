package com.example.hara_gym.data.model

import com.google.gson.annotations.SerializedName

data class ExerciseDto(
    val id: Int,
    val name: String,
    val sets: String,
    val reps: String
)

data class WorkoutDayDto(
    val id: Int,
    @SerializedName("day_name") val dayName: String,
    val title: String,
    val description: String,
    @SerializedName("video_url") val videoUrl: String,
    val exercises: List<ExerciseDto>
)

data class WorkoutPlanDto(
    val id: Int,
    val name: String,
    val description: String?,
    @SerializedName("is_active") val isActive: Boolean,
    val days: List<WorkoutDayDto>
)
