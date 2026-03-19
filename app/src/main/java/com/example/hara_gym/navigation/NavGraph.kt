package com.example.hara_gym.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hara_gym.ui.screens.*
import com.example.hara_gym.ui.viewmodel.AuthViewModel
import com.example.hara_gym.ui.viewmodel.WorkoutViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object WorkoutDetail : Screen("workoutDetail/{dayName}") {
        fun createRoute(dayName: String) = "workoutDetail/$dayName"
    }
}

@Composable
fun NavGraph(navController: NavHostController, startDestination: String) {
    val authViewModel: AuthViewModel = viewModel()
    val workoutViewModel: WorkoutViewModel = viewModel()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = authViewModel,
                onDayClick = { dayName ->
                    navController.navigate(Screen.WorkoutDetail.createRoute(dayName))
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.WorkoutDetail.route) { backStackEntry ->
            val dayName = backStackEntry.arguments?.getString("dayName") ?: ""
            WorkoutDetailScreen(
                dayName = dayName,
                viewModel = workoutViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
