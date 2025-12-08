package com.example.alignment_tool.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CamberDao {
    @Insert
    suspend fun insert(measurement: CamberMeasurement)

    @Query("SELECT * FROM camber_measurements ORDER BY date DESC")
    fun getAll(): Flow<List<CamberMeasurement>>
}
