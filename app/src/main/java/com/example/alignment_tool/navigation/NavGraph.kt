package com.example.alignment_tool.navigation

import AppTheme
import SettingsScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.alignment_tool.ui.screen.CamberScreen
import com.example.alignment_tool.ui.screen.ToeScreen
import androidx.compose.ui.Modifier
import com.example.alignment_tool.ui.screen.CasterScreen
import com.example.alignment_tool.ui.theme.ThemeViewModel

sealed class Screen(val route: String, val title: String) {
    object Toe : Screen("toe", "Toe")
    object Camber : Screen("camber", "Camber")
    object Caster : Screen("caster", "Caster")
    object Settings : Screen("settings", "Settings")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Toe.route,
        modifier = modifier
    ) {
        composable(Screen.Toe.route) { ToeScreen() }
        composable(Screen.Camber.route) { CamberScreen() }
        composable(Screen.Caster.route) { CasterScreen() }
        composable(Screen.Settings.route) {
            val theme by themeViewModel.currentTheme.collectAsState()
            SettingsScreen(
                currentTheme = theme,
                onThemeSelected = { themeViewModel.selectTheme(it) }
            )
        }
    }
}
