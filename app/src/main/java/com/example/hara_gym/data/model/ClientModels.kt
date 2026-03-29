package com.example.hara_gym.data.model

import com.google.gson.annotations.SerializedName

data class MyPlansResponse(
    @SerializedName("workout_plan") val workoutPlan: WorkoutPlanDto?,
    @SerializedName("diet_plan") val dietPlan: DietPlanDto?
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
    @SerializedName("client_id") val clientId: Int,
    val age: Int,
    @SerializedName("weight_kg") val weightKg: String,
    @SerializedName("height_cm") val heightCm: String,
    @SerializedName("workout_frequency") val workoutFrequency: String,
    val goals: String,
    val status: String,
    @SerializedName("created_at") val createdAt: String
)
