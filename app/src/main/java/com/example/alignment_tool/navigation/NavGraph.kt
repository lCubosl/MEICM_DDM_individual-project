package com.example.alignment_tool.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.alignment_tool.ui.screen.CamberScreen
import com.example.alignment_tool.ui.screen.ToeScreen
import com.example.alignment_tool.ui.screen.SavedValuesScreen
import androidx.compose.ui.Modifier
import com.example.alignment_tool.data.repository.CamberRepository
import com.example.alignment_tool.ui.screen.SettingsScreen
import com.example.alignment_tool.data.viewmodel.ThemeViewModel
import com.example.alignment_tool.data.viewmodel.ToeViewModel
import com.example.alignment_tool.data.repository.ToeRepository
import com.example.alignment_tool.data.viewmodel.CamberViewModel

sealed class Screen(val route: String, val title: String) {
    object Toe : Screen("toe", "Toe")
    object Camber : Screen("camber", "Camber")
    object SavedValues : Screen("savedValues", "Saved")
    object Settings : Screen("settings", "Settings")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel,
    toeViewModel: ToeViewModel,
    toeRepository: ToeRepository,
    camberViewModel: CamberViewModel,
    camberRepository: CamberRepository
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Toe.route,
        modifier = modifier
    ) {

        composable(Screen.Toe.route) {
            ToeScreen(viewModel = toeViewModel)
        }

        composable(Screen.Camber.route) {
            CamberScreen(viewModel = camberViewModel)
        }

        composable(Screen.SavedValues.route) {
            SavedValuesScreen(repoToe = toeRepository, repoCamber= camberRepository)
        }

        composable(Screen.Settings.route) {
            val theme by themeViewModel.currentTheme.collectAsState()
            SettingsScreen(
                currentTheme = theme,
                onThemeSelected = { themeViewModel.selectTheme(it) }
            )
        }
    }
}
