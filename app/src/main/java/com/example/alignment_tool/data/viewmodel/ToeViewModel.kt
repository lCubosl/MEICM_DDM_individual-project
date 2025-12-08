package com.example.alignment_tool.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alignment_tool.data.db.ToeMeasurement
import com.example.alignment_tool.data.repository.ToeRepository
import kotlinx.coroutines.launch

class ToeViewModel(private val repo: ToeRepository) : ViewModel() {

    val allMeasurements = repo.getAll() // Flow<List<ToeMeasurement>>

    fun saveMeasurement(
        flAngle: Float?, frAngle: Float?,
        rlAngle: Float?, rrAngle: Float?,
        fToe: Float?, rToe: Float?
    ) {
        val measurement = ToeMeasurement(
            date = System.currentTimeMillis(),
            flAngle = flAngle ?: 0f,
            frAngle = frAngle ?: 0f,
            rlAngle = rlAngle ?: 0f,
            rrAngle = rrAngle ?: 0f,
            fToe = fToe ?: 0f,
            rToe = rToe ?: 0f
        )
        viewModelScope.launch {
            repo.save(measurement)
        }
    }
}