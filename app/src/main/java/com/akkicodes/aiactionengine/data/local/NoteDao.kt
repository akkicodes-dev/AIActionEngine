package com.akkicodes.aiactionengine.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {

    // 💾 Note insert
    @Insert
    suspend fun insert(note: Note)

    // 📜 All notes fetch
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    suspend fun getAllNotes(): List<Note>

    // 🔍 Search by tag (🔥 NEW)
    @Query("SELECT * FROM notes WHERE tags LIKE '%' || :query || '%'")
    suspend fun searchByTag(query: String): List<Note>
}