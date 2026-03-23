package com.example.hara_gym.data.model

import com.google.gson.annotations.SerializedName

data class DietMealDto(
    val id: Int,
    @SerializedName("meal_name") val mealName: String,
    @SerializedName("meal_time") val mealTime: String,
    val foods: String,
    val notes: String?
)

data class DietPlanDto(
    val id: Int,
    val name: String,
    val description: String?,
    val is_active: Boolean,
    val meals: List<DietMealDto>
)
