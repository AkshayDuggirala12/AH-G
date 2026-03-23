package com.example.hara_gym.data.repository

import com.example.hara_gym.data.api.ApiService
import com.example.hara_gym.data.api.TokenManager
import com.example.hara_gym.data.model.LoginRequest
import com.example.hara_gym.data.model.LoginResponse
import com.example.hara_gym.data.model.RegisterRequest
import com.example.hara_gym.data.model.UserDto
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun register(request: RegisterRequest): Response<UserDto> {
        return apiService.register(request)
    }

    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        val response = apiService.login(request)
        if (response.isSuccessful) {
            response.body()?.access_token?.let { token ->
                tokenManager.saveToken(token)
            }
        }
        return response
    }

    suspend fun logout() {
        tokenManager.clearToken()
    }

    fun getToken() = tokenManager.token
}
