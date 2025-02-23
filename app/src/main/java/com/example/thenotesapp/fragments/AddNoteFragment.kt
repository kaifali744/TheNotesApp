package com.example.thenotesapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.thenotesapp.MainActivity
import com.example.thenotesapp.R
import com.example.thenotesapp.databinding.FragmentAddNoteBinding
import com.example.thenotesapp.model.Note
import com.example.thenotesapp.viewmodel.NoteViewModel

// This fragment is used for adding a new note to the app.
// It includes a form to input the note's title and description and save it.
class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {

    // Binding object for the fragment layout to access views safely and easily.
    private var addNoteBinding: FragmentAddNoteBinding? = null
    private val binding get() = addNoteBinding!! // Ensures non-null access to the binding.

    // ViewModel to manage the data for notes.
    private lateinit var notesViewModel: NoteViewModel

    // Variable to hold the root view of the fragment.
    private lateinit var addNoteView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment and initialize the binding object.
        addNoteBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root // Return the root view of the layout.
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add options menu to the fragment.
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Initialize the ViewModel from MainActivity.
        notesViewModel = (activity as MainActivity).noteViewModel

        // Store the fragment's root view for later use (e.g., navigation).
        addNoteView = view
    }

    // Function to save the note entered by the user.
    private fun saveNote(view: View) {
        // Get the title and description entered by the user.
        val noteTitle = binding.addNoteTitle.text.toString().trim() // Remove extra spaces.
        val noteDesc = binding.addNoteDesc.text.toString().trim()

        // Check if the title is not empty (mandatory field).
        if (noteTitle.isNotEmpty()) {
            // Create a new Note object with the provided title and description.
            val note = Note(0, noteTitle, noteDesc) // '0' indicates the ID will be auto-generated.

            // Add the note to the database through the ViewModel.
            notesViewModel.addNote(note)

            // Show a confirmation message to the user.
            Toast.makeText(addNoteView.context, "Note Added Successfully!", Toast.LENGTH_SHORT).show()

            // Navigate back to the HomeFragment after saving the note.
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            // Show a message asking the user to enter a title if it's empty.
            Toast.makeText(addNoteView.context, "Please enter Note title!", Toast.LENGTH_SHORT).show()
        }
    }

    // Called to create the options menu for this fragment.
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear() // Clear any existing menu items from the previous fragment.
        menuInflater.inflate(R.menu.menu_add_note, menu) // Inflate the menu for this fragment.
    }

    // Called when a menu item is selected.
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.saveMenu -> { // Check if the "Save" menu item was clicked.
                saveNote(addNoteView) // Call the saveNote function to save the note.
                true // Indicate that the menu item click was handled.
            }
            else -> false // Return false for unhandled menu items.
        }
    }

    // Cleanup to prevent memory leaks when the fragment is destroyed.
    override fun onDestroy() {
        super.onDestroy()
        addNoteBinding = null // Set the binding object to null to release resources.
    }
}
