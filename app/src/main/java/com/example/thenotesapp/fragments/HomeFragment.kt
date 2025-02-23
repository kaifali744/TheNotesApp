package com.example.thenotesapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.thenotesapp.MainActivity
import com.example.thenotesapp.R
import com.example.thenotesapp.adapter.NoteAdapter
import com.example.thenotesapp.databinding.FragmentHomeBinding
import com.example.thenotesapp.model.Note
import com.example.thenotesapp.viewmodel.NoteViewModel

// HomeFragment is the main screen of the app where all notes are displayed in a grid layout.
// It supports search functionality and provides a FloatingActionButton to add new notes.
class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener, MenuProvider {

    // Variable to hold the binding object for this fragment's layout.
    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!! // Ensures non-null access to the binding object.

    // ViewModel to manage the notes data.
    private lateinit var notesViewModel: NoteViewModel

    // Adapter to bind the list of notes to the RecyclerView.
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment and initialize the binding object.
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root // Return the root view of the inflated layout.
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adding menu options to the fragment.
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Initializing the ViewModel from the MainActivity.
        notesViewModel = (activity as MainActivity).noteViewModel

        // Setting up the RecyclerView for displaying notes.
        setupHomeRecyclerView()

        // Handling click event for the FloatingActionButton to navigate to the AddNoteFragment.
        binding.addNoteFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }

    // Method to update the UI based on the notes list.
    private fun updateUI(note: List<Note>?) {
        if (note != null) {
            if (note.isEmpty()) {
                // Show the "no notes" image if the list is empty.
                binding.emptyNotesImage.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            } else {
                // Show the RecyclerView if notes are available.
                binding.emptyNotesImage.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    // Method to configure the RecyclerView.
    private fun setupHomeRecyclerView() {
        noteAdapter = NoteAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) // 2-column grid.
            setHasFixedSize(true) // Optimization for fixed-size RecyclerView.
            adapter = noteAdapter
        }

        // Observing all notes from ViewModel and submitting the list to the adapter.
        activity?.let {
            notesViewModel.getAllNotes().observe(viewLifecycleOwner) { note ->
                noteAdapter.differ.submitList(note) // Submit the list to DiffUtil for efficient updates.
                updateUI(note) // Update the UI based on the list.
            }
        }
    }

    // Method to handle search queries and filter notes.
    private fun searchNote(query: String?) {
        val searchQuery = "%$query" // Append '%' for SQLite LIKE query.
        notesViewModel.searchNote(searchQuery).observe(this) { list ->
            noteAdapter.differ.submitList(list) // Update the adapter with filtered notes.
        }
    }

    // SearchView callback for handling search submission.
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false // Return false as no specific action is taken on submission.
    }

    // SearchView callback for handling text change in the search bar.
    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchNote(newText) // Call searchNote method to filter the list.
        }
        return true // Return true to indicate the text change is handled.
    }

    // Cleaning up the binding object to avoid memory leaks.
    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null // Set the binding to null when the fragment is destroyed.
    }

    // Inflating the menu and setting up the search functionality.
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear() // Clear any existing menu items.
        menuInflater.inflate(R.menu.home_menu, menu) // Inflate the menu file for this fragment.

        // Configure the search menu item.
        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false // Disable the submit button in the SearchView.
        menuSearch.setOnQueryTextListener(this) // Set this fragment as the listener for search events.
    }

    // Handling menu item clicks (currently returns false as no item is handled).
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}
