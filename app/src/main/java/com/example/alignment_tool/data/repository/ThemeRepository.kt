package com.example.alignment_tool.data.repository

import android.content.Context
import com.example.alignment_tool.data.datastore.ThemeDataStore
import com.example.alignment_tool.data.model.ThemeOption
import kotlinx.coroutines.flow.Flow

class ThemeRepository(context: Context) {
    private val dataStore = ThemeDataStore(context)
    val themeFlow: Flow<ThemeOption> = dataStore.themeFlow
    suspend fun saveTheme(option: ThemeOption) {
        dataStore.saveTheme(option)
    }
}
