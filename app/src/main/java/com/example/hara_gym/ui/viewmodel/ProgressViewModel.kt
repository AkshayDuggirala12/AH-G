package com.example.hara_gym.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hara_gym.data.model.ToggleExerciseRequest
import com.example.hara_gym.data.model.WeeklyProgressItemDto
import com.example.hara_gym.data.model.WorkoutDayProgressDto
import com.example.hara_gym.data.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProgressUiState {
    object Loading : ProgressUiState()
    data class Success(val weekly: List<WeeklyProgressItemDto>) : ProgressUiState()
    data class Error(val message: String) : ProgressUiState()
}

sealed class DayProgressUiState {
    object Loading : DayProgressUiState()
    data class Success(val dayProgress: WorkoutDayProgressDto) : DayProgressUiState()
    data class Error(val message: String) : DayProgressUiState()
}

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val repository: ProgressRepository
) : ViewModel() {

    private val _weeklyState = MutableStateFlow<ProgressUiState>(ProgressUiState.Loading)
    val weeklyState: StateFlow<ProgressUiState> = _weeklyState.asStateFlow()

    private val _dayState = MutableStateFlow<DayProgressUiState>(DayProgressUiState.Loading)
    val dayState: StateFlow<DayProgressUiState> = _dayState.asStateFlow()

    fun fetchWeeklyProgress(progressDate: String? = null) {
        viewModelScope.launch {
            _weeklyState.value = ProgressUiState.Loading
            try {
                val response = repository.getWeeklyProgress(progressDate)
                if (response.isSuccessful) {
                    _weeklyState.value = ProgressUiState.Success(response.body() ?: emptyList())
                } else {
                    _weeklyState.value = ProgressUiState.Error("Failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _weeklyState.value = ProgressUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchDayProgress(dayName: String, progressDate: String? = null) {
        viewModelScope.launch {
            _dayState.value = DayProgressUiState.Loading
            try {
                val response = repository.getDayProgress(dayName, progressDate)
                if (response.isSuccessful && response.body() != null) {
                    _dayState.value = DayProgressUiState.Success(response.body()!!)
                } else {
                    _dayState.value = DayProgressUiState.Error("Failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _dayState.value = DayProgressUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun toggleExercise(exerciseId: Int, isCompleted: Boolean, dayName: String, progressDate: String? = null) {
        viewModelScope.launch {
            try {
                val response = repository.toggleExerciseProgress(
                    ToggleExerciseRequest(exerciseId, isCompleted, progressDate)
                )
                if (response.isSuccessful && response.body() != null) {
                    _dayState.value = DayProgressUiState.Success(response.body()!!)
                    // Optionally refresh weekly progress
                    // fetchWeeklyProgress(progressDate)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
