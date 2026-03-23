package com.akkicodes.aiactionengine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.akkicodes.aiactionengine.di.DatabaseProvider
import com.akkicodes.aiactionengine.ui.screens.HomeScreen
import com.akkicodes.aiactionengine.ui.theme.AIActionEngineTheme
import com.akkicodes.aiactionengine.viewmodel.NoteViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseProvider.getDatabase(this)
        val dao = db.noteDao()
        val viewModel = NoteViewModel(dao)

        setContent {
            AIActionEngineTheme {
                HomeScreen(viewModel)
            }
        }
    }
}