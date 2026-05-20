package com.firman.directoryuser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.firman.directoryuser.core.navigation.NavGraph
import com.firman.directoryuser.core.theme.UserDirectoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            val navController = rememberNavController()

            val barColor = android.graphics.Color.TRANSPARENT
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(barColor, barColor) { isDarkMode },
                navigationBarStyle = SystemBarStyle.auto(barColor, barColor) { isDarkMode }
            )
            
            UserDirectoryTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavGraph(
                        navController = navController,
                        isDarkMode = isDarkMode,
                        onThemeToggle = { isDarkMode = !isDarkMode }
                    )
                }
            }
        }
    }
}
