package com.akkicodes.aiactionengine.di

import android.content.Context
import androidx.room.Room
import com.akkicodes.aiactionengine.data.local.AppDatabase

object DatabaseProvider {

    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "notes_db"
            )
                .fallbackToDestructiveMigration()  // 🔥 ADD THIS
                .build()
            INSTANCE = instance
            instance
        }
    }
}