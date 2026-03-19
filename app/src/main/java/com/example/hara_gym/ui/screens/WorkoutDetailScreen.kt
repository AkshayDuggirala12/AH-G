package com.example.hara_gym.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.hara_gym.ui.viewmodel.WorkoutState
import com.example.hara_gym.ui.viewmodel.WorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailScreen(
    dayName: String,
    viewModel: WorkoutViewModel,
    onBack: () -> Unit
) {
    val workoutState by viewModel.workoutState
    val context = LocalContext.current

    LaunchedEffect(dayName) {
        viewModel.getWorkoutByDay(dayName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(dayName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = workoutState) {
                is WorkoutState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is WorkoutState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
                is WorkoutState.Success -> {
                    val workout = state.workout
                    if (workout == null) {
                        Text(
                            text = "No workout found for $dayName",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            item {
                                Text(text = workout.title, style = MaterialTheme.typography.headlineSmall)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = workout.description, style = MaterialTheme.typography.bodyLarge)
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(workout.videoUrl))
                                    context.startActivity(intent)
                                }) {
                                    Text("Watch Video")
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(text = "Exercises", style = MaterialTheme.typography.titleLarge)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            items(workout.exercises) { exercise ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = exercise.name, style = MaterialTheme.typography.titleMedium)
                                        Text(text = "Sets: ${exercise.sets}, Reps: ${exercise.reps}", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
