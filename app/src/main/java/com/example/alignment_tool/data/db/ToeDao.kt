package com.example.alignment_tool.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ToeDao {
    @Insert
    suspend fun insert(toe: ToeMeasurement)

    @Query("SELECT * FROM toe_measurements ORDER BY date DESC")
    fun getAllMeasurements(): Flow<List<ToeMeasurement>>

    @Query("DELETE FROM toe_measurements")
    suspend fun clearAll()
}
