package com.example.alignment_tool.data.viewmodel

import com.example.alignment_tool.data.datastore.CarPreferences
import com.example.alignment_tool.data.repository.CarRepository

class CarViewModelFactory(
    private val repo: CarRepository,
    private val prefs: CarPreferences
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarViewModel(repo, prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
