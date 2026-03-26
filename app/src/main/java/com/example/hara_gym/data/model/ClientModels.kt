package com.example.hara_gym.data.model

import com.google.gson.annotations.SerializedName

data class MyPlansResponse(
    @SerializedName("workout_plan") val workoutPlan: WorkoutPlanDto?,
    @SerializedName("diet_plan") val dietPlan: DietPlanDto?
)
