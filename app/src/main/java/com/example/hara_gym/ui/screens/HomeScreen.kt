package com.example.hara_gym.ui.screens

import android.view.LayoutInflater
import android.view.View
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hara_gym.databinding.LayoutHomeBinding
import com.example.hara_gym.ui.viewmodel.ClientUiState
import com.example.hara_gym.ui.viewmodel.ClientViewModel

@Composable
fun HomeScreen(
    onNavigateToWorkout: () -> Unit,
    onNavigateToDiet: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAccessRequest: () -> Unit,
    clientViewModel: ClientViewModel = hiltViewModel()
) {
    val clientState by clientViewModel.uiState.collectAsState()

    // --- UI INFLATION AND LOGIC ---
    AndroidView(
        factory = { context ->
            LayoutHomeBinding.inflate(LayoutInflater.from(context)).apply {
                // NAVIGATION ACTIONS
                cardWorkout.setOnClickListener { onNavigateToWorkout() }
                cardDiet.setOnClickListener { onNavigateToDiet() }
                
                // Note: The menu item 'action_profile' was not found in XML.
                // If you want to handle profile navigation, ensure the menu is inflated
                // or use a different UI element.
                
                // This is a custom action for when no plan is found
                btnRequestAccess.setOnClickListener { onNavigateToAccessRequest() }
            }.root
        },
        update = { view ->
            val binding = LayoutHomeBinding.bind(view)
            // --- API STATE TO UI MAPPING ---
            when (val state = clientState) {
                is ClientUiState.Loading -> {
                    binding.loadingIndicator.visibility = View.VISIBLE
                    binding.containerSuccess.visibility = View.GONE
                    binding.containerNoPlan.visibility = View.GONE
                    binding.containerPending.visibility = View.GONE
                }
                is ClientUiState.NoPlanAssigned -> {
                    binding.loadingIndicator.visibility = View.GONE
                    binding.containerSuccess.visibility = View.GONE
                    binding.containerNoPlan.visibility = View.VISIBLE
                    binding.containerPending.visibility = View.GONE
                }
                is ClientUiState.AccessRequestPending -> {
                    binding.loadingIndicator.visibility = View.GONE
                    binding.containerSuccess.visibility = View.GONE
                    binding.containerNoPlan.visibility = View.GONE
                    binding.containerPending.visibility = View.VISIBLE
                }
                is ClientUiState.Success -> {
                    binding.loadingIndicator.visibility = View.GONE
                    binding.containerSuccess.visibility = View.VISIBLE
                    binding.containerNoPlan.visibility = View.GONE
                    binding.containerPending.visibility = View.GONE
                    
                    binding.textWorkoutSubtitle.text = state.plans.workoutPlan?.name ?: "No active plan"
                    binding.textDietSubtitle.text = state.plans.dietPlan?.name ?: "Check meals"
                }
                is ClientUiState.Error -> {
                    binding.loadingIndicator.visibility = View.GONE
                    // Error handling could show a Toast or Snackbar here
                }
            }
        }
    )
}
