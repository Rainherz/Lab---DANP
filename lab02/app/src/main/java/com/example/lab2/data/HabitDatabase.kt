package com.example.lab2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HabitEntity::class, HabitLogEntity::class], version = 1, exportSchema = false)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        @Volatile
        private var Instance: HabitDatabase? = null

        fun getDatabase(context: Context): HabitDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    HabitDatabase::class.java,
                    "habit_database"
                )
                .build()
                .also { Instance = it }
            }
        }
    }
}
