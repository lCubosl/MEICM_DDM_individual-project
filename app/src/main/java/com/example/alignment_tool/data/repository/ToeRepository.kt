package com.example.alignment_tool.data.repository

import com.example.alignment_tool.data.db.ToeDao
import com.example.alignment_tool.data.db.ToeMeasurement
import kotlinx.coroutines.flow.Flow

class ToeRepository(private val dao: ToeDao) {
    fun getAll(): Flow<List<ToeMeasurement>> = dao.getAllMeasurements()
    suspend fun save(measurement: ToeMeasurement) = dao.insert(measurement)
    suspend fun clear() = dao.clearAll()
}
