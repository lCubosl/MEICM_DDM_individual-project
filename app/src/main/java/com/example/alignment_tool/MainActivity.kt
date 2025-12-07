package com.example.alignment_tool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.example.alignment_tool.navigation.NavGraph
import com.example.alignment_tool.ui.components.BottomNavigationBar
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.alignment_tool.ui.theme.ThemeViewModel
import com.example.alignment_tool.ui.theme.ThemeRepository
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Step 1: Create repository and ViewModel
            val repo = ThemeRepository(applicationContext)
            val vm = remember { ThemeViewModel(repo) }

            // Step 2: Collect current theme
            val theme by vm.currentTheme.collectAsState()

            // Step 3: Set color scheme based on theme
            val colorScheme = when (theme) {
                AppTheme.LIGHT -> lightColorScheme()
                AppTheme.DARK -> darkColorScheme()
                AppTheme.SYSTEM -> if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
            }

            MaterialTheme(colorScheme = colorScheme) {
                val navController = rememberNavController()

                Scaffold(bottomBar = { BottomNavigationBar(navController) }) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        themeViewModel = vm
                    )
                }
            }
        }
    }
}

