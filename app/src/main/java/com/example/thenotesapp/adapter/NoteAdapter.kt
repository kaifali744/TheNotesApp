package com.example.thenotesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.thenotesapp.databinding.NoteLayoutBinding
import com.example.thenotesapp.fragments.HomeFragmentDirections
import com.example.thenotesapp.model.Note

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    // ViewHolder class to bind and manage each note layout
    class NoteViewHolder(val itemBinding: NoteLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root)

    // DiffUtil helps compare two lists efficiently to update RecyclerView
    private val differCallback = object : DiffUtil.ItemCallback<Note>() {

        // Checks if two Note objects are the same by comparing their IDs
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        // Checks if the contents (title, description, etc.) of two Note objects are the same
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    // AsyncListDiffer is used to manage and calculate differences in the list efficiently
    val differ = AsyncListDiffer(this, differCallback)

    // Called when a new ViewHolder needs to be created for a RecyclerView item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        // Inflate the layout for each note item and create a ViewHolder
        return NoteViewHolder(
            NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    // Returns the total number of notes in the current list
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // Binds the data to the ViewHolder for each position in the list
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        // Get the current note from the list
        val currentNote = differ.currentList[position]

        // Set the note title and description in the layout
        holder.itemBinding.noteTitle.text = currentNote.noteTitle
        holder.itemBinding.noteDesc.text = currentNote.noteDesc

        // Handle item click to navigate to the EditNoteFragment with the selected note's details
        holder.itemView.setOnClickListener {
            // Create a navigation action with the current note as an argument
            val direction = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(currentNote)

            // Navigate to the EditNoteFragment
            it.findNavController().navigate(direction)
        }
    }
}
