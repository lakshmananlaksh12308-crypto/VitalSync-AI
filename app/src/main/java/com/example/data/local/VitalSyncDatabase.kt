package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ReportEntity::class, BiomarkerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class VitalSyncDatabase : RoomDatabase() {
    abstract fun vitalSyncDao(): VitalSyncDao

    companion object {
        @Volatile
        private var INSTANCE: VitalSyncDatabase? = null

        fun getDatabase(context: Context): VitalSyncDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VitalSyncDatabase::class.java,
                    "vitalsync_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
