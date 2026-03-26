package com.example.hara_gym.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hara_gym.data.model.MyPlansResponse
import com.example.hara_gym.data.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ClientUiState {
    object Loading : ClientUiState()
    data class Success(val plans: MyPlansResponse) : ClientUiState()
    data class Error(val message: String) : ClientUiState()
}

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val repository: ClientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ClientUiState>(ClientUiState.Loading)
    val uiState: StateFlow<ClientUiState> = _uiState.asStateFlow()

    init {
        fetchMyPlans()
    }

    fun fetchMyPlans() {
        viewModelScope.launch {
            _uiState.value = ClientUiState.Loading
            try {
                val response = repository.getMyPlans()
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = ClientUiState.Success(response.body()!!)
                } else {
                    _uiState.value = ClientUiState.Error("Failed to fetch plans: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = ClientUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
