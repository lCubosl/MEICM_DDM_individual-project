package com.example.alignment_tool.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ToeMeasurement::class, CamberMeasurement::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toeDao(): ToeDao
    abstract fun camberDao(): CamberDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "alignment_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

