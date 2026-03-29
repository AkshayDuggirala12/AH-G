package com.example.hara_gym.ui.screens

import android.view.LayoutInflater
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hara_gym.databinding.LayoutRegisterBinding
import com.example.hara_gym.ui.viewmodel.AuthState
import com.example.hara_gym.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()

    // --- API SUCCESS LOGIC ---
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onRegisterSuccess()
            viewModel.resetState()
        }
    }

    // --- UI INFLATION AND API CALLING ---
    AndroidView(
        factory = { context ->
            LayoutRegisterBinding.inflate(LayoutInflater.from(context)).apply {
                // API CALL: Register trigger
                buttonRegister.setOnClickListener {
                    val name = editTextName.text.toString()
                    val email = editTextEmail.text.toString()
                    val password = editTextPassword.text.toString()
                    viewModel.register(name, email, password)
                }

                // NAVIGATION LOGIC
                buttonToLogin.setOnClickListener {
                    onNavigateToLogin()
                }
            }.root
        },
        update = { view ->
            val binding = LayoutRegisterBinding.bind(view)
            // UI FEEDBACK LOGIC
            val isLoading = authState is AuthState.Loading
            binding.buttonRegister.isEnabled = !isLoading
            binding.buttonRegister.text = if (isLoading) "CREATING ACCOUNT..." else "REGISTER"
        }
    )
}
