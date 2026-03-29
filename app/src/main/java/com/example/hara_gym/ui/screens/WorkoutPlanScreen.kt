package com.example.hara_gym.ui.screens

import android.view.LayoutInflater
import android.view.View
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hara_gym.databinding.LayoutWorkoutPlanBinding
import com.example.hara_gym.ui.adapter.WorkoutDayAdapter
import com.example.hara_gym.ui.viewmodel.WorkoutUiState
import com.example.hara_gym.ui.viewmodel.WorkoutViewModel

@Composable
fun WorkoutPlanScreen(
    onNavigateToDayDetails: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: WorkoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val workoutAdapter = remember { 
        WorkoutDayAdapter { day -> onNavigateToDayDetails(day.dayName) } 
    }

    AndroidView(
        factory = { context ->
            LayoutWorkoutPlanBinding.inflate(LayoutInflater.from(context)).apply {
                toolbar.setNavigationOnClickListener { onBack() }
                
                recyclerViewDays.layoutManager = LinearLayoutManager(context)
                recyclerViewDays.adapter = workoutAdapter
            }.root
        },
        update = { view ->
            val binding = LayoutWorkoutPlanBinding.bind(view)
            
            when (val state = uiState) {
                is WorkoutUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is WorkoutUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    workoutAdapter.submitList(state.plan?.days)
                }
                is WorkoutUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    )
}
