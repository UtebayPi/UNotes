package com.utebayKazAlm.todolist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.utebayKazAlm.todolist.R
import com.utebayKazAlm.todolist.data.Note
import com.utebayKazAlm.todolist.databinding.FragmentNoteBinding

class NoteListAdapter(
    private val clickListener: (Note) -> Unit
) : ListAdapter<Note, NoteListAdapter.NoteViewHolder>(DiffCallback) {

    class NoteViewHolder(
        private var binding: FragmentNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.note = note
            if (note.checked != null) {
                binding.checked.visibility = View.VISIBLE
                binding.checked.text =
                    this.itemView.context.getString(if (note.checked) R.string.done else R.string.not_done)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NoteViewHolder(
            FragmentNoteBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener(note)
        }
        holder.bind(note)
    }
}