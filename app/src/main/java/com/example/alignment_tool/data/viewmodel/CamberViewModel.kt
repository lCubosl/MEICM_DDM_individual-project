package com.example.alignment_tool.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alignment_tool.data.repository.CamberRepository
import com.example.alignment_tool.data.db.CamberMeasurement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CamberViewModel(private val repo: CamberRepository) : ViewModel() {
    val allMeasurements: Flow<List<CamberMeasurement>> = repo.getAll()

    fun saveMeasurement(fl: Float?, fr: Float?, rl: Float?, rr: Float?) {
        val measurement = CamberMeasurement(
            date = System.currentTimeMillis(),
            flCamber = fl ?: 0f,
            frCamber = fr ?: 0f,
            rlCamber = rl ?: 0f,
            rrCamber = rr ?: 0f
        )
        viewModelScope.launch {
            repo.save(measurement)
        }
    }
}
