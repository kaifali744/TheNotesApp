package com.example.thenotesapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.thenotesapp.repository.NoteRepository

// A custom ViewModelFactory class to provide dependencies to the ViewModel
class NoteViewModelFactory(
    val app: Application, // Application context to pass to the ViewModel
    private val noteRepository: NoteRepository // Repository to manage data-related operations
) : ViewModelProvider.Factory {

    // Overriding the create method to construct and return the ViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the ViewModel class matches the required NoteViewModel class
        return NoteViewModel(app, noteRepository) as T
        // The above line creates an instance of NoteViewModel and provides the
        // application context and repository as its dependencies.
        // The "as T" is used to cast the object to the generic type T.
    }
}
