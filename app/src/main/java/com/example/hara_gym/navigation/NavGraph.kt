package com.example.hara_gym.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hara_gym.ui.screens.*
import com.example.hara_gym.ui.viewmodel.*

@Composable
fun NavGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val sessionState by authViewModel.sessionState.collectAsState()

    when (sessionState) {
        is SessionState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else -> {
            val startDestination = if (sessionState is SessionState.Authenticated) {
                Screen.Home.route
            } else {
                Screen.Login.route
            }

            NavHost(navController = navController, startDestination = startDestination) {
                composable(Screen.Login.route) {
                    LoginScreen(
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
                        onNavigateToWorkout = { navController.navigate(Screen.WorkoutPlan.route) },
                        onNavigateToDiet = { navController.navigate(Screen.DietPlan.route) },
                        onNavigateToProgress = { navController.navigate(Screen.WeeklyProgress.route) },
                        onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
                    )
                }
                composable(Screen.WorkoutPlan.route) {
                    WorkoutPlanScreen(
                        onNavigateToDayDetails = { dayName ->
                            navController.navigate(Screen.WorkoutDayDetails.createRoute(dayName))
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.WorkoutDayDetails.route) { backStackEntry ->
                    val dayName = backStackEntry.arguments?.getString("dayName") ?: ""
                    WorkoutDayDetailsScreen(
                        dayName = dayName,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.DietPlan.route) {
                    DietPlanScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.WeeklyProgress.route) {
                    WeeklyProgressScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(
                        onLogout = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
