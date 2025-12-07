package com.example.alignment_tool.ui.theme

import AppTheme
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

// DataStore delegate
val Context.themeDataStore by preferencesDataStore("settings")

class ThemeRepository(private val context: Context) {

    companion object {
        val THEME_KEY = stringPreferencesKey("app_theme")
    }

    // Flow to observe the saved theme
    val themeFlow = context.themeDataStore.data.map { prefs ->
        val savedName = prefs[THEME_KEY]
        savedName?.let { AppTheme.valueOf(it) } ?: AppTheme.SYSTEM
    }

    // Save theme
    suspend fun saveTheme(theme: AppTheme) {
        context.themeDataStore.edit { prefs ->
            prefs[THEME_KEY] = theme.name
        }
    }
}
