package com.example.hara_gym.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hara_gym.ui.viewmodel.AuthViewModel
import com.example.hara_gym.ui.viewmodel.ClientUiState
import com.example.hara_gym.ui.viewmodel.ClientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToWorkout: () -> Unit,
    onNavigateToDiet: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAccessRequest: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    clientViewModel: ClientViewModel = hiltViewModel()
) {
    val clientState by clientViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Rajakshari",
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    ) 
                },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            Icons.Default.AccountCircle, 
                            contentDescription = "Profile",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "WELCOME BACK,",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.secondary,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "CHAMPION",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            when (val state = clientState) {
                is ClientUiState.NoPlanAssigned -> {
                    item {
                        AccessRequestCard(onNavigateToAccessRequest)
                    }
                }
                is ClientUiState.AccessRequestPending -> {
                    item {
                        PendingRequestCard(state.request.status)
                    }
                }
                is ClientUiState.Success -> {
                    item {
                        HomeCard(
                            title = "WORKOUT PLAN",
                            subtitle = state.plans.workoutPlan?.name ?: "No active workout plan",
                            icon = Icons.Default.Upcoming,
                            onClick = onNavigateToWorkout,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    item {
                        HomeCard(
                            title = "DIET PLAN",
                            subtitle = state.plans.dietPlan?.name ?: "No active diet plan",
                            icon = Icons.Default.Restaurant,
                            onClick = onNavigateToDiet
                        )
                    }

                    item {
                        HomeCard(
                            title = "PROGRESS TRACKER",
                            subtitle = "Check your weekly stats",
                            icon = Icons.Default.ShowChart,
                            onClick = onNavigateToProgress
                        )
                    }
                }
                is ClientUiState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                is ClientUiState.Error -> {
                    item {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { clientViewModel.checkStatus() }) {
                            Text("Retry")
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                QuoteCard()
            }
        }
    }
}

@Composable
fun AccessRequestCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "NO PLAN ASSIGNED",
                fontWeight = FontWeight.Black,
                fontSize = 18.sp
            )
            Text(
                text = "You don't have an active workout or diet plan yet. Click here to submit an access request to our trainers.",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onClick) {
                Text("REQUEST ACCESS")
            }
        }
    }
}

@Composable
fun PendingRequestCard(status: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column {
                Text(
                    text = "REQUEST PENDING",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "Your access request is currently being reviewed by our trainers. Please check back soon!",
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun HomeCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (containerColor == MaterialTheme.colorScheme.surface) 
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) 
                    else Color.White.copy(alpha = 0.2f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon, 
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = if (containerColor == MaterialTheme.colorScheme.surface)
                            MaterialTheme.colorScheme.primary
                            else Color.White
                    )
                }
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title, 
                    fontSize = 18.sp, 
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = subtitle, 
                    fontSize = 13.sp, 
                    fontWeight = FontWeight.Medium,
                    color = contentColor.copy(alpha = 0.7f)
                )
            }
            
            Icon(
                Icons.Default.ChevronRight, 
                contentDescription = null,
                tint = contentColor.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun QuoteCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = "\"THE ONLY BAD WORKOUT IS THE ONE THAT DIDN'T HAPPEN.\"",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Keep pushing forward.",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
