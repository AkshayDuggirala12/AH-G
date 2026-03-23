package com.example.hara_gym.data.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    val is_active: Boolean,
    val is_admin: Boolean
)

data class LoginResponse(
    val access_token: String,
    val token_type: String,
    val user: UserDto
)
