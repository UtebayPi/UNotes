package com.utebaypi.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.utebaypi.todolist.R
import com.utebaypi.todolist.data.room.Note
import com.utebaypi.todolist.databinding.FragmentAddEditBinding
import com.utebaypi.todolist.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditFragment : Fragment() {
    private var _binding: FragmentAddEditBinding? = null
    private val binding get() = _binding!!
    private val navArgs: AddEditFragmentArgs by navArgs()
    private val viewModel: ListViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val id = navArgs.id
        // Checking if we are creating a new entry or editing the existing one.
        if (id > 0) {
            updateNote(id)
        } else {
            addNote()
        }
    }

    private fun addNote() {
        binding.actionButton.text = getString(R.string.save)
        binding.actionButton.setOnClickListener {
            //Verifying that note's formatting is valid.
            val valid = viewModel.addNote(getNoteFromInput())
            if (valid)
                findNavController().navigate(AddEditFragmentDirections.actionAddEditFragmentToListFragment())
        }
    }


    private fun updateNote(id: Int) {
        binding.actionButton.text = getString(R.string.update)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getNote(id).collectLatest { note: Note? ->
                    if (note == null) {
                        findNavController().popBackStack()
                        return@collectLatest
                    }
                    //put the existing note's values into the fields
                    binding.apply {
                        if (note.checked != null) checkBox.isChecked = true
                        titleInput.setText(note.title, TextView.BufferType.SPANNABLE)
                        contentInput.setText(note.content, TextView.BufferType.SPANNABLE)
                    }
                    //updating the note
                    binding.actionButton.setOnClickListener {
                        val valid = viewModel.updateNote(getNoteFromInput(id))
                        if (valid)
                            findNavController().popBackStack()
                    }

                }
            }
        }
    }

    //Creating a note from input
    private fun getNoteFromInput(id: Int = 0): Note {
        return Note(
            id = if (id > 0) id else 0,
            title = binding.titleInput.text.toString().trim(),
            content = binding.contentInput.text.toString().trim(),
            checked = if (binding.checkBox.isChecked) false else null
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}