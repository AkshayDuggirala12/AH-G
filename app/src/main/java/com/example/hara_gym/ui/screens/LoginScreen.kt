package com.example.hara_gym.ui.screens

import android.view.LayoutInflater
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hara_gym.databinding.LayoutLoginBinding
import com.example.hara_gym.ui.viewmodel.AuthState
import com.example.hara_gym.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()

    // --- API SUCCESS LOGIC ---
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess()
            viewModel.resetState()
        }
    }

    // --- UI INFLATION AND API CALLING ---
    AndroidView(
        factory = { context ->
            LayoutLoginBinding.inflate(LayoutInflater.from(context)).apply {
                // API CALL: Login trigger
                buttonLogin.setOnClickListener {
                    val email = editTextEmail.text.toString()
                    val password = editTextPassword.text.toString()
                    viewModel.login(email, password)
                }

                // NAVIGATION LOGIC
                buttonToRegister.setOnClickListener {
                    onNavigateToRegister()
                }
            }.root
        },
        update = { view ->
            val binding = LayoutLoginBinding.bind(view)
            // UI FEEDBACK LOGIC
            val isLoading = authState is AuthState.Loading
            binding.buttonLogin.isEnabled = !isLoading
            binding.buttonLogin.text = if (isLoading) "VERIFYING..." else "LOGIN"
        }
    )
}
