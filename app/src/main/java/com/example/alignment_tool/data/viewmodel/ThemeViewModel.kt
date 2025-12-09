package com.example.alignment_tool.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alignment_tool.data.model.ThemeOption
import com.example.alignment_tool.data.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val repo: ThemeRepository
) : ViewModel() {

    val currentTheme: Flow<ThemeOption> = repo.themeFlow

    fun setTheme(option: ThemeOption) {
        viewModelScope.launch {
            repo.saveTheme(option)
        }
    }
}
