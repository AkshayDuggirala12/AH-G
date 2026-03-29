package com.example.hara_gym.data.model

import com.google.gson.annotations.SerializedName

data class MyPlansResponse(
    @SerializedName("workout_plan") val workoutPlan: WorkoutPlanDto? = null,
    @SerializedName("diet_plan") val dietPlan: DietPlanDto? = null
)

data class ClientAccessRequestBody(
    val age: Int,
    @SerializedName("weight_kg") val weightKg: String,
    @SerializedName("height_cm") val heightCm: String,
    @SerializedName("workout_frequency") val workoutFrequency: String,
    val goals: String
)

data class ClientAccessRequestDto(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    val age: Int,
    @SerializedName("weight_kg") val weightKg: String,
    @SerializedName("height_cm") val heightCm: String,
    @SerializedName("workout_frequency") val workoutFrequency: String,
    val goals: String,
    val status: String,
    @SerializedName("created_at") val createdAt: String
)
