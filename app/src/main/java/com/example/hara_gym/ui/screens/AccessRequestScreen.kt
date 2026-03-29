package com.example.hara_gym.ui.screens

import android.view.LayoutInflater
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hara_gym.databinding.LayoutAccessRequestBinding
import com.example.hara_gym.ui.viewmodel.AccessRequestState
import com.example.hara_gym.ui.viewmodel.ClientViewModel

@Composable
fun AccessRequestScreen(
    onBack: () -> Unit,
    viewModel: ClientViewModel = hiltViewModel()
) {
    val state by viewModel.accessRequestState.collectAsState()

    // --- API SUCCESS LOGIC ---
    LaunchedEffect(state) {
        if (state is AccessRequestState.Success) {
            onBack()
        }
    }

    // --- UI INFLATION AND API CALLING ---
    AndroidView(
        factory = { context ->
            LayoutAccessRequestBinding.inflate(LayoutInflater.from(context)).apply {
                // API CALL: Submit Request
                buttonSubmit.setOnClickListener {
                    val age = editTextAge.text.toString().toIntOrNull() ?: 0
                    val weight = editTextWeight.text.toString()
                    val height = editTextHeight.text.toString()
                    val frequency = editTextFrequency.text.toString()
                    val goals = editTextGoals.text.toString()

                    viewModel.submitAccessRequest(age, weight, height, frequency, goals)
                }

                // NAVIGATION
                toolbar.setNavigationOnClickListener { onBack() }
            }.root
        },
        update = { view ->
            val binding = LayoutAccessRequestBinding.bind(view)
            // UI FEEDBACK LOGIC
            val isLoading = state is AccessRequestState.Loading
            binding.buttonSubmit.isEnabled = !isLoading
            binding.buttonSubmit.text = if (isLoading) "SUBMITTING..." else "SUBMIT REQUEST"
        }
    )
}
