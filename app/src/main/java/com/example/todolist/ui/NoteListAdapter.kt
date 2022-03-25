package com.example.todolist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.Note
import com.example.todolist.databinding.FragmentNoteBinding

class NoteListAdapter(
    private val clickListener: (Note) -> Unit
) : ListAdapter<Note, NoteListAdapter.ForageableViewHolder>(DiffCallback) {

    class ForageableViewHolder(
        private var binding: FragmentNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.note = note
            if (note.checked != null) {
                binding.checked.visibility = View.VISIBLE
                binding.checked.text = if (note.checked) "Сделано" else "Не выполнено"
            } else {
                binding.checked.visibility = View.GONE
            }
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForageableViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ForageableViewHolder(
            FragmentNoteBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ForageableViewHolder, position: Int) {
        val forageable = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener(forageable)
        }
        holder.bind(forageable)
    }
}