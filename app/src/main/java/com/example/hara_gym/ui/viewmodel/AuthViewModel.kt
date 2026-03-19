package com.example.hara_gym.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hara_gym.data.api.RetrofitClient
import com.example.hara_gym.data.api.TokenManager
import com.example.hara_gym.data.model.LoginRequest
import com.example.hara_gym.data.model.RegisterRequest
import com.example.hara_gym.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WorkoutRepository(RetrofitClient.apiService)
    private val tokenManager = TokenManager(application)

    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)
    val authState: State<AuthState> = _authState

    private val _isLoggedIn = mutableStateOf<Boolean?>(null)
    val isLoggedIn: State<Boolean?> = _isLoggedIn

    init {
        viewModelScope.launch {
            val token = tokenManager.token.firstOrNull()
            _isLoggedIn.value = !token.isNullOrEmpty()
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repository.register(RegisterRequest(name, email, password))
                if (response.isSuccessful) {
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("Registration failed: ${response.code()}")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repository.login(LoginRequest(email, password))
                if (response.isSuccessful && response.body() != null) {
                    tokenManager.saveToken(response.body()!!.accessToken)
                    _isLoggedIn.value = true
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("Login failed: ${response.code()}")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearToken()
            _isLoggedIn.value = false
        }
    }
}
