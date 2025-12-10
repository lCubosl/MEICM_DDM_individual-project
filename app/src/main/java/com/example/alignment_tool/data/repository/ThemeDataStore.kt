package com.example.alignment_tool.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.alignment_tool.data.model.ThemeOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "theme_prefs")

class ThemeDataStore(private val context: Context) {

    companion object {
        private val THEME_KEY = stringPreferencesKey("selected_theme")
    }

    val themeFlow: Flow<ThemeOption> = context.dataStore.data.map { prefs ->
        val saved = prefs[THEME_KEY]
        ThemeOption.entries.find { it.name == saved } ?: ThemeOption.SYSTEM
    }

    suspend fun saveTheme(option: ThemeOption) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = option.name
        }
    }
}
