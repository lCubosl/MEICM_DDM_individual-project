package com.example.alignment_tool.data.repository

import com.example.alignment_tool.data.db.CamberDao
import com.example.alignment_tool.data.db.CamberMeasurement
import kotlinx.coroutines.flow.Flow

class CamberRepository(private val dao: CamberDao) {
    fun getAll(): Flow<List<CamberMeasurement>> = dao.getAll()

    suspend fun save(measurement: CamberMeasurement) = dao.insert(measurement)
}
