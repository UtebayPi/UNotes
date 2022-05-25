package com.utebayKazAlm.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.utebayKazAlm.todolist.R
import com.utebayKazAlm.todolist.data.Note
import com.utebayKazAlm.todolist.databinding.FragmentAddEditBinding
import com.utebayKazAlm.todolist.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditFragment : Fragment() {
    private var _binding: FragmentAddEditBinding? = null
    private val binding get() = _binding!!
    private val navArgs: AddEditFragmentArgs by navArgs()

    private val viewModel: ListViewModel by activityViewModels()
//    {
//        ListViewModel.Factory((activity?.application as BaseApplication).database.noteDao())
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val id = navArgs.id
        if (id > 0) {
            updateNote(id)
        } else {
            addNote()
        }
    }

    private fun addNote() {
        binding.actionButton.text = getString(R.string.save)
        binding.actionButton.setOnClickListener {
            val valid = viewModel.addNote(getNoteFromInput())
            if (!valid) return@setOnClickListener
            findNavController().navigate(AddEditFragmentDirections.actionAddEditFragmentToListFragment())
        }
    }

    private fun updateNote(id: Int) {
        binding.actionButton.text = getString(R.string.update)
        viewModel.getNote(id).observe(viewLifecycleOwner) { note: Note? ->
            if (note == null) {
                findNavController().popBackStack()
                return@observe
            }
            if (note.checked != null) binding.checkBox.isChecked = true
            binding.titleInput.setText(note.title, TextView.BufferType.SPANNABLE)
            binding.contentInput.setText(note.content, TextView.BufferType.SPANNABLE)
            binding.actionButton.setOnClickListener {
                val valid = viewModel.updateNote(getNoteFromInput(id))
                if (!valid) return@setOnClickListener
                findNavController().popBackStack()
            }

        }
    }

    private fun getNoteFromInput(id: Int = 0): Note{
        return Note(
            id = if(id>0) id else 0,
            title = binding.titleInput.text.toString().trim(),
            content = binding.contentInput.text.toString().trim(),
            checked = if (binding.checkBox.isChecked) false else null)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}