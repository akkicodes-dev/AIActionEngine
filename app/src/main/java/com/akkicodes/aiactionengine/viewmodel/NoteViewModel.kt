package com.akkicodes.aiactionengine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akkicodes.aiactionengine.data.local.Note
import com.akkicodes.aiactionengine.data.local.NoteDao
import com.akkicodes.aiactionengine.data.remote.AiProcessor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteViewModel(
    private val dao: NoteDao
) : ViewModel() {

    // 🔥 UI ko data dene ke liye StateFlow use kar rahe hai
    // ye reactive hai → data change hoga to UI auto update hoga
    private val _notes = MutableStateFlow<List<Note>>(emptyList())

    // 🔹 UI sirf read karega (safe)
    val notes: StateFlow<List<Note>> = _notes

    // 🤖 AI Processor instance
    private val ai = AiProcessor()

    // 🔥 init → jab ViewModel start hota hai tab run hota hai
    init {
        loadNotes()
    }

    // 🧠 DB se saare notes load karna
    private fun loadNotes() {
        viewModelScope.launch {
            _notes.value = dao.getAllNotes()
        }
    }

    // 🔥 MAIN FUNCTION → note add karna
    fun addNote(content: String) {
        viewModelScope.launch {

            // 🤖 STEP 1: AI ko call karte hai
            val (summary, tags) = ai.processNote(content)

            // 🧱 STEP 2: Note object banate hai
            val note = Note(
                content = summary,   // AI se aaya summary
                tags = tags,         // AI se aaye tags
                timestamp = System.currentTimeMillis()
            )

            // 💾 STEP 3: DB me save
            dao.insert(note)

            // 🔄 STEP 4: UI ko refresh
            loadNotes()
        }
    }

    // 🔍 SEARCH FUNCTION (tags ke basis pe)
    fun searchNotes(query: String) {
        viewModelScope.launch {

            // 🧠 DB se filtered data nikaal rahe hai
            _notes.value = if (query.isBlank()) {
                dao.getAllNotes()   // empty → sab dikhao
            } else {
                dao.searchByTag(query)  // filter by tag
            }
        }
    }
}