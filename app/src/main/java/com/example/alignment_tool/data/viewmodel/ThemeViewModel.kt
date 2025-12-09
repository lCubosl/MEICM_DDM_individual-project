package com.example.alignment_tool.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alignment_tool.data.repository.ThemeRepository
import com.example.alignment_tool.ui.screen.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val repo: ThemeRepository
) : ViewModel() {

    private val _currentTheme = MutableStateFlow(AppTheme.SYSTEM)
    val currentTheme: StateFlow<AppTheme> = _currentTheme

    fun selectTheme(theme: AppTheme) {
        viewModelScope.launch {
            _currentTheme.value = theme
            repo.saveTheme(theme)  // if you store it in DataStore
        }
    }
}
