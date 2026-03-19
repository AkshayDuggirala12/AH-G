package com.example.hara_gym.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hara_gym.data.api.RetrofitClient
import com.example.hara_gym.data.api.TokenManager
import com.example.hara_gym.data.model.Workout
import com.example.hara_gym.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class WorkoutState {
    object Loading : WorkoutState()
    data class Success(val workout: Workout?) : WorkoutState()
    data class Error(val message: String) : WorkoutState()
}

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WorkoutRepository(RetrofitClient.apiService)
    private val tokenManager = TokenManager(application)

    private val _workoutState = mutableStateOf<WorkoutState>(WorkoutState.Loading)
    val workoutState: State<WorkoutState> = _workoutState

    fun getWorkoutByDay(dayName: String) {
        viewModelScope.launch {
            _workoutState.value = WorkoutState.Loading
            try {
                val token = tokenManager.token.first()
                if (token != null) {
                    val response = repository.getWorkoutByDay(token, dayName.lowercase())
                    if (response.isSuccessful) {
                        _workoutState.value = WorkoutState.Success(response.body())
                    } else {
                        _workoutState.value = WorkoutState.Error("Failed to fetch workout: ${response.code()}")
                    }
                } else {
                    _workoutState.value = WorkoutState.Error("No authentication token found.")
                }
            } catch (e: Exception) {
                _workoutState.value = WorkoutState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
