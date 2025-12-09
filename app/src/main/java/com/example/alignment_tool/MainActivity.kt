package com.example.alignment_tool

import com.example.alignment_tool.data.model.ThemeOption
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.alignment_tool.navigation.NavGraph
import com.example.alignment_tool.ui.components.BottomNavigationBar
import com.example.alignment_tool.data.viewmodel.ThemeViewModel
import com.example.alignment_tool.data.repository.ThemeRepository
import com.example.alignment_tool.data.db.AppDatabase
import com.example.alignment_tool.data.repository.ToeRepository
import com.example.alignment_tool.data.viewmodel.ToeViewModel
import com.example.alignment_tool.data.viewmodel.ToeViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alignment_tool.data.datastore.CarPreferences
import com.example.alignment_tool.data.remote.CarApiClient
import com.example.alignment_tool.data.repository.CamberRepository
import com.example.alignment_tool.data.repository.CarRepository
import com.example.alignment_tool.data.viewmodel.CamberViewModel
import com.example.alignment_tool.data.viewmodel.CamberViewModelFactory
import com.example.alignment_tool.data.viewmodel.CarViewModel
import com.example.alignment_tool.data.viewmodel.CarViewModelFactory
import com.example.alignment_tool.data.viewmodel.ThemeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // FOR THE LOVE OF GOD THIS SHOULD GO A .ENV
            val api = CarApiClient.createService(
                apiKey = "21334d2c-b7cb-498e-9402-e115645ecf23",
                apiSecret = "6235b270ef22d10e211808bfdff7b76e"
            )

            // CAR API
            val carRepo = CarRepository(api)
            val carPrefs = CarPreferences(this)
            val carVM: CarViewModel = viewModel(
                factory = CarViewModelFactory(carRepo, carPrefs)
            )

            // THEME VIEWMODEL
            val themeRepo = ThemeRepository(applicationContext)
            val themeVM: ThemeViewModel = viewModel(factory = ThemeViewModelFactory(themeRepo))

            val theme by themeVM.currentTheme.collectAsState(initial = ThemeOption.SYSTEM)

            val colorScheme = when (theme) {
                ThemeOption.LIGHT -> lightColorScheme()
                ThemeOption.DARK -> darkColorScheme()
                ThemeOption.SYSTEM ->
                    if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
            }

            // TOE VIEWMODEL (with DB)
            val context = LocalContext.current
            val db = remember { AppDatabase.getDatabase(context) }
            val toeRepo = remember { ToeRepository(db.toeDao()) }
            val camberRepo = remember { CamberRepository(db.camberDao()) }

            val toeVM: ToeViewModel = viewModel(
                factory = ToeViewModelFactory(toeRepo)
            )
            val camberVM: CamberViewModel = viewModel(
                factory = CamberViewModelFactory(camberRepo)
            )

            MaterialTheme(colorScheme = colorScheme) {

                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        themeViewModel = themeVM,
                        toeViewModel = toeVM,
                        toeRepository = toeRepo,
                        camberViewModel = camberVM,
                        camberRepository = camberRepo, // <--- THIS WAS MISSING
                        carViewModel = carVM
                    )
                }
            }
        }
    }
}
