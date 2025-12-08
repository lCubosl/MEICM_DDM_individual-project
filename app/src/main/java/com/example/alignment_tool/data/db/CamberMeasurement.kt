package com.example.alignment_tool.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "camber_measurements")
data class CamberMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val flCamber: Float,
    val frCamber: Float,
    val rlCamber: Float,
    val rrCamber: Float
)
