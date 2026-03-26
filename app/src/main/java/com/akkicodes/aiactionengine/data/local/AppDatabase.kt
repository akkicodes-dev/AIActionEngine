package com.akkicodes.aiactionengine.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class],
    version = 2   // 🔥 VERSION BUMP (important)
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}