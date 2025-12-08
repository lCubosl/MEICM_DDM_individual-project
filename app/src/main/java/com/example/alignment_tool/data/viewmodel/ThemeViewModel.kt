package com.example.alignment_tool.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alignment_tool.ui.screen.AppTheme
import com.example.alignment_tool.data.repository.ThemeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(private val repo: ThemeRepository) : ViewModel() {

    // Current theme as a StateFlow
    val currentTheme = repo.themeFlow.stateIn(
        viewModelScope,
        SharingStarted.Companion.Eagerly,
        AppTheme.SYSTEM
    )

    // Change theme and persist it
    fun selectTheme(theme: AppTheme) {
        viewModelScope.launch {
            repo.saveTheme(theme)
        }
    }
}