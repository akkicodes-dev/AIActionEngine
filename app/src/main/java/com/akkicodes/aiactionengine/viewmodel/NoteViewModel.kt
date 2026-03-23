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

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    // 🤖 AI instance
    private val ai = AiProcessor()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            _notes.value = dao.getAllNotes()
        }
    }

    fun addNote(content: String) {
        viewModelScope.launch {

            // 🤖 AI processing step
            val processedText = ai.processNote(content)

            val note = Note(
                content = processedText,
                timestamp = System.currentTimeMillis()
            )

            dao.insert(note)
            loadNotes()
        }
    }
}