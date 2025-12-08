package com.example.alignment_tool.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alignment_tool.data.repository.ToeRepository

class ToeViewModelFactory(
    private val repo: ToeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ToeViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}