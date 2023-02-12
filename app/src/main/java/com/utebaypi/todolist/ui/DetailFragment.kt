package com.utebaypi.todolist.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.utebaypi.todolist.R
import com.utebaypi.todolist.data.room.Note
import com.utebaypi.todolist.databinding.FragmentDetailBinding
import com.utebaypi.todolist.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val navArgs: DetailFragmentArgs by navArgs()

    private val viewModel: ListViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val id = navArgs.id
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getNote(id).collectLatest { note: Note? ->
                    if (note == null) {
                        findNavController().popBackStack()
                    } else {
                        bindNote(note)
                    }
                }
            }
        }

    }

    private fun bindNote(note: Note) {
        binding.apply {
            titleText.text = note.title
            contentText.text = note.content
        }
        //Checking what is the type of the note. If it's a task (checked != null)
        //then we make the checkbox visible, give it the note's checked value, and when
        //we press, change the checked value in note.
        if (note.checked != null) {
            binding.isDone.visibility = View.VISIBLE
            binding.isDone.isChecked = note.checked
            binding.isDone.setOnCheckedChangeListener { button, b ->
                viewModel.updateNote(note.copy(checked = b))
            }
        } else { // If it's just a note (checked = null), then we hide the checkbox
            binding.isDone.visibility = View.GONE
        }
        binding.deleteButton.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(requireContext())
            builder.setMessage(getString(R.string.question))
                .setPositiveButton(getString(R.string.yes)) { dialogInterface, i ->
                    viewModel.deleteNote(note)
                    findNavController().popBackStack()
                }
                .setNegativeButton(getString(R.string.no)) { dialogInterface, i -> }
            builder.create().show()

        }
        binding.editButton.setOnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToAddEditFragment(note.id)
            findNavController().navigate(action)
        }


    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}