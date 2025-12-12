package com.example.alignment_tool.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TimeToLeave
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.alignment_tool.data.model.ThemeOption
import com.example.alignment_tool.data.repository.CamberRepository
import com.example.alignment_tool.ui.screen.SettingsScreen
import com.example.alignment_tool.data.viewmodel.ThemeViewModel
import com.example.alignment_tool.data.viewmodel.ToeViewModel
import com.example.alignment_tool.data.repository.ToeRepository
import com.example.alignment_tool.data.viewmodel.CamberViewModel
import com.example.alignment_tool.data.viewmodel.CarViewModel

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Toe : Screen("toe", "Toe", Icons.Default.CarRepair)
    object Camber : Screen("camber", "Camber", Icons.Default.TimeToLeave)
    object SavedValues : Screen("savedValues", "Saved", Icons.Default.Save)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel,
    toeViewModel: ToeViewModel,
    toeRepository: ToeRepository,
    camberViewModel: CamberViewModel,
    camberRepository: CamberRepository,
    carViewModel: CarViewModel
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
            val theme by themeViewModel.currentTheme.collectAsState(initial = ThemeOption.SYSTEM)

            SettingsScreen(
                currentTheme = theme,
                onThemeSelected = { themeViewModel.setTheme(it) },
                carViewModel = carViewModel
            )
        }

    }
}
