package com.example.hara_gym.ui.screens

import android.view.LayoutInflater
import android.view.View
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hara_gym.R
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

    AndroidView(
        factory = { context ->
            LayoutHomeBinding.inflate(LayoutInflater.from(context)).apply {
                toolbar.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_profile -> {
                            onNavigateToProfile()
                            true
                        }
                        else -> false
                    }
                }
                
                cardWorkout.setOnClickListener { onNavigateToWorkout() }
                cardDiet.setOnClickListener { onNavigateToDiet() }
                btnRequestAccess.setOnClickListener { onNavigateToAccessRequest() }
            }.root
        },
        update = { view ->
            val binding = LayoutHomeBinding.bind(view)
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
                }
            }
        }
    )
}
