package com.example.alignment_tool.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.alignment_tool.ui.screen.CamberScreen
import com.example.alignment_tool.ui.screen.ToeScreen
import androidx.compose.ui.Modifier
import com.example.alignment_tool.ui.screen.CasterScreen

sealed class Screen(val route: String, val title: String) {
    object Toe : Screen("toe", "Toe")
    object Camber : Screen("camber", "Camber")
    object Caster : Screen("caster", "Caster")
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Toe.route,
        modifier = modifier
    ) {
        composable(Screen.Toe.route) { ToeScreen() }
        composable(Screen.Camber.route) { CamberScreen() }
        composable(Screen.Caster.route) { CasterScreen() }
    }
}
