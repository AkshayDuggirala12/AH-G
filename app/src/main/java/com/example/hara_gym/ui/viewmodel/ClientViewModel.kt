package com.example.hara_gym.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hara_gym.data.model.ClientAccessRequestBody
import com.example.hara_gym.data.model.ClientAccessRequestDto
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
    object NoPlanAssigned : ClientUiState()
    data class AccessRequestPending(val request: ClientAccessRequestDto) : ClientUiState()
    data class Error(val message: String) : ClientUiState()
}

sealed class AccessRequestState {
    object Idle : AccessRequestState()
    object Loading : AccessRequestState()
    object Success : AccessRequestState()
    data class Error(val message: String) : AccessRequestState()
}

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val repository: ClientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ClientUiState>(ClientUiState.Loading)
    val uiState: StateFlow<ClientUiState> = _uiState.asStateFlow()

    private val _accessRequestState = MutableStateFlow<AccessRequestState>(AccessRequestState.Idle)
    val accessRequestState: StateFlow<AccessRequestState> = _accessRequestState.asStateFlow()

    init {
        checkStatus()
    }

    fun checkStatus() {
        viewModelScope.launch {
            _uiState.value = ClientUiState.Loading
            try {
                val plansResponse = repository.getMyPlans()
                if (plansResponse.isSuccessful && plansResponse.body() != null) {
                    val plans = plansResponse.body()!!
                    if (plans.workoutPlan != null || plans.dietPlan != null) {
                        _uiState.value = ClientUiState.Success(plans)
                    } else {
                        checkAccessRequests()
                    }
                } else if (plansResponse.code() == 404) {
                    checkAccessRequests()
                } else {
                    _uiState.value = ClientUiState.Error("Failed to fetch plans")
                }
            } catch (e: Exception) {
                _uiState.value = ClientUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private suspend fun checkAccessRequests() {
        try {
            val requestResponse = repository.getMyAccessRequests()
            if (requestResponse.isSuccessful && !requestResponse.body().isNullOrEmpty()) {
                // Take the latest request
                val latest = requestResponse.body()!!.first()
                if (latest.status == "pending") {
                    _uiState.value = ClientUiState.AccessRequestPending(latest)
                } else {
                    _uiState.value = ClientUiState.NoPlanAssigned
                }
            } else {
                _uiState.value = ClientUiState.NoPlanAssigned
            }
        } catch (e: Exception) {
            _uiState.value = ClientUiState.NoPlanAssigned
        }
    }

    fun submitAccessRequest(age: Int, weight: String, height: String, frequency: String, goals: String) {
        viewModelScope.launch {
            _accessRequestState.value = AccessRequestState.Loading
            try {
                val request = ClientAccessRequestBody(age, weight, height, frequency, goals)
                val response = repository.submitAccessRequest(request)
                if (response.isSuccessful) {
                    _accessRequestState.value = AccessRequestState.Success
                    checkStatus() // Refresh status
                } else {
                    _accessRequestState.value = AccessRequestState.Error("Failed to submit: ${response.message()}")
                }
            } catch (e: Exception) {
                _accessRequestState.value = AccessRequestState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
