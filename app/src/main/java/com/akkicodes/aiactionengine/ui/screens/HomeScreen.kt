package com.akkicodes.aiactionengine.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.akkicodes.aiactionengine.viewmodel.NoteViewModel

@Composable
fun HomeScreen(viewModel: NoteViewModel) {

    // 🔄 reactive data
    val notes by viewModel.notes.collectAsState()

    // 📝 input state
    var text by remember { mutableStateOf("") }

    // 🔍 search state
    var search by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {

        // 🔍 SEARCH BAR
        TextField(
            value = search,
            onValueChange = {
                search = it
                viewModel.searchNotes(it) // 🔥 live filtering
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search by tag...") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 📝 INPUT FIELD
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Write your note...") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ➕ ADD BUTTON
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    viewModel.addNote(text)
                    text = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Note")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 📜 NOTES LIST
        LazyColumn {
            items(notes) { note ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        // 🧠 SUMMARY
                        Text(
                            text = note.content,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // 🏷️ TAG CHIPS
                        val tags = note.tags.split(",")

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            tags.forEach { tag ->

                                Box(
                                    modifier = Modifier
                                        .padding(end = 6.dp)
                                        .background(
                                            color = Color(0xFF444444),
                                            shape = RoundedCornerShape(50)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = tag.trim(),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}