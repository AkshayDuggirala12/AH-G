package com.example.hara_gym.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hara_gym.data.model.WorkoutPlanDto
import com.example.hara_gym.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WorkoutUiState {
    object Loading : WorkoutUiState()
    data class Success(val plan: WorkoutPlanDto?) : WorkoutUiState()
    data class Error(val message: String) : WorkoutUiState()
}

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WorkoutUiState>(WorkoutUiState.Loading)
    val uiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()

    init {
        fetchWorkoutPlan()
    }

    fun fetchWorkoutPlan() {
        viewModelScope.launch {
            _uiState.value = WorkoutUiState.Loading
            try {
                val response = repository.getMyWorkoutPlan()
                if (response.isSuccessful) {
                    _uiState.value = WorkoutUiState.Success(response.body())
                } else if (response.code() == 404) {
                    _uiState.value = WorkoutUiState.Success(null)
                } else {
                    _uiState.value = WorkoutUiState.Error("Failed to fetch plan: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = WorkoutUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
