package com.example.hara_gym

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.hara_gym.navigation.NavGraph
import com.example.hara_gym.ui.theme.HaragymTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setHighRefreshRate()
        
        enableEdgeToEdge()
        setContent {
            HaragymTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }

    private fun setHighRefreshRate() {
        val maxRefreshRate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display?.supportedModes?.maxByOrNull { it.refreshRate }?.refreshRate ?: 60f
        } else {
            val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
            @Suppress("DEPRECATION")
            val display = windowManager.defaultDisplay
            display.supportedModes.maxByOrNull { it.refreshRate }?.refreshRate ?: 60f
        }

        if (maxRefreshRate > 60f) {
            window.attributes = window.attributes.apply {
                preferredRefreshRate = maxRefreshRate
            }
        }
    }
}
