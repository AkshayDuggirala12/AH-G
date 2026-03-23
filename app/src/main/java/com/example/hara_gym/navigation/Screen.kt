package com.example.hara_gym.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object WorkoutPlan : Screen("workout_plan")
    object WorkoutDayDetails : Screen("workout_day/{dayName}") {
        fun createRoute(dayName: String) = "workout_day/$dayName"
    }
    object DietPlan : Screen("diet_plan")
    object WeeklyProgress : Screen("weekly_progress")
    object Profile : Screen("profile")
}
