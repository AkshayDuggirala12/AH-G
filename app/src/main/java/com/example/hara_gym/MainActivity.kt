package com.example.hara_gym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.hara_gym.data.api.TokenManager
import com.example.hara_gym.navigation.NavGraph
import com.example.hara_gym.navigation.Screen
import com.example.hara_gym.ui.theme.HaragymTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HaragymTheme {
                val navController = rememberNavController()
                val tokenManager = TokenManager(applicationContext)
                val token = tokenManager.token.collectAsState(initial = null).value

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (token != null) {
                        NavGraph(navController, startDestination = Screen.Home.route)
                    } else {
                        NavGraph(navController, startDestination = Screen.Login.route)
                    }
                }
            }
        }
    }
}
