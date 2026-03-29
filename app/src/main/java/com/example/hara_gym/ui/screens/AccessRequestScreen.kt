package com.example.hara_gym.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hara_gym.ui.viewmodel.AccessRequestState
import com.example.hara_gym.ui.viewmodel.ClientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessRequestScreen(
    onBack: () -> Unit,
    viewModel: ClientViewModel = hiltViewModel()
) {
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var goals by remember { mutableStateOf("") }

    val state by viewModel.accessRequestState.collectAsState()

    LaunchedEffect(state) {
        if (state is AccessRequestState.Success) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("REQUEST PLAN", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Personal Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Fill in your details to get a personalized workout and diet plan from our trainers.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Height (cm)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            OutlinedTextField(
                value = frequency,
                onValueChange = { frequency = it },
                label = { Text("Workout Frequency (e.g., 3 times/week)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = goals,
                onValueChange = { goals = it },
                label = { Text("Your Goals (e.g., Fat loss, Muscle gain)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val ageInt = age.toIntOrNull() ?: 0
                    viewModel.submitAccessRequest(ageInt, weight, height, frequency, goals)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = state !is AccessRequestState.Loading
            ) {
                if (state is AccessRequestState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("SUBMIT REQUEST", fontWeight = FontWeight.Bold)
                }
            }

            if (state is AccessRequestState.Error) {
                Text(
                    text = (state as AccessRequestState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
