package com.example.hara_gym.ui.screens

import android.view.LayoutInflater
import android.view.View
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hara_gym.databinding.LayoutWorkoutDayDetailsBinding
import com.example.hara_gym.ui.adapter.ExerciseAdapter
import com.example.hara_gym.ui.viewmodel.DayProgressUiState
import com.example.hara_gym.ui.viewmodel.ProgressViewModel

@Composable
fun WorkoutDayDetailsScreen(
    dayName: String,
    onBack: () -> Unit,
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val uiState by viewModel.dayState.collectAsState()
    val exerciseAdapter = remember {
        ExerciseAdapter { exercise ->
            viewModel.toggleExercise(exercise.exerciseId, !exercise.isCompleted, dayName)
        }
    }

    LaunchedEffect(dayName) {
        viewModel.fetchDayProgress(dayName)
    }

    AndroidView(
        factory = { context ->
            LayoutWorkoutDayDetailsBinding.inflate(LayoutInflater.from(context)).apply {
                toolbar.setNavigationOnClickListener { onBack() }
                toolbar.title = dayName.uppercase()
                
                recyclerViewExercises.layoutManager = LinearLayoutManager(context)
                recyclerViewExercises.adapter = exerciseAdapter
            }.root
        },
        update = { view ->
            val binding = LayoutWorkoutDayDetailsBinding.bind(view)
            
            when (val state = uiState) {
                is DayProgressUiState.Loading -> {
                    // Show a loading indicator if added to XML
                }
                is DayProgressUiState.Success -> {
                    val progress = state.dayProgress
                    binding.textViewDayTitle.text = progress.title
                    binding.textViewProgressPercentage.text = "${progress.percentage.toInt()}%"
                    binding.progressIndicator.progress = progress.percentage.toInt()
                    binding.textViewProgressStatus.text = "${progress.completedExercises} of ${progress.totalExercises} exercises completed"
                    
                    exerciseAdapter.submitList(progress.exercises)
                }
                is DayProgressUiState.Error -> {
                    // Handle error
                }
            }
        }
    )
}
