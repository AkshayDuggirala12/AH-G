package com.example.hara_gym.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hara_gym.data.model.DietPlanDto
import com.example.hara_gym.data.repository.DietRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DietUiState {
    object Loading : DietUiState()
    data class Success(val plan: DietPlanDto?) : DietUiState()
    data class Error(val message: String) : DietUiState()
}

@HiltViewModel
class DietViewModel @Inject constructor(
    private val repository: DietRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DietUiState>(DietUiState.Loading)
    val uiState: StateFlow<DietUiState> = _uiState.asStateFlow()

    init {
        fetchDietPlan()
    }

    fun fetchDietPlan() {
        viewModelScope.launch {
            _uiState.value = DietUiState.Loading
            try {
                val response = repository.getMyDietPlan()
                if (response.isSuccessful) {
                    _uiState.value = DietUiState.Success(response.body())
                } else if (response.code() == 404) {
                    _uiState.value = DietUiState.Success(null)
                } else {
                    _uiState.value = DietUiState.Error("Failed to fetch plan: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = DietUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
