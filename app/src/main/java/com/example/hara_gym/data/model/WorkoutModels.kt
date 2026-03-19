package com.example.hara_gym.data.model

import com.google.gson.annotations.SerializedName

data class Workout(
    val id: Int,
    @SerializedName("day_name") val dayName: String,
    val title: String,
    val description: String,
    @SerializedName("video_url") val videoUrl: String,
    val exercises: List<Exercise>
)

data class Exercise(
    val id: Int,
    val name: String,
    val sets: String,
    val reps: String
)
