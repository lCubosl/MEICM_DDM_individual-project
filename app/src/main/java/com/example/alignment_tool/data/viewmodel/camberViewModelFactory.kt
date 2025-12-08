package com.example.alignment_tool.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alignment_tool.data.repository.CamberRepository

class CamberViewModelFactory(
    private val repo: CamberRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CamberViewModel::class.java)) {
            return CamberViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
