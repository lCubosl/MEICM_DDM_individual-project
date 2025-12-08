package com.example.alignment_tool.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "toe_measurements")
data class ToeMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long = System.currentTimeMillis(),
    val flAngle: Float,
    val frAngle: Float,
    val rlAngle: Float,
    val rrAngle: Float,
    val fToe: Float,
    val rToe: Float
)
