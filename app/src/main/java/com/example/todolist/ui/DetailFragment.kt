package com.example.todolist.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.BaseApplication
import com.example.todolist.R
import com.example.todolist.data.Note
import com.example.todolist.databinding.FragmentDetailBinding
import com.example.todolist.viewmodel.ListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val navArgs: DetailFragmentArgs by navArgs()

    private val viewModel: ListViewModel by activityViewModels {
        ListViewModel.Factory((activity?.application as BaseApplication).database.noteDao())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val id = navArgs.id
        viewModel.getNote(id).observe(viewLifecycleOwner) { note: Note? ->
            if (note == null) {
                findNavController().popBackStack()
            } else {
                bindNote(note)
            }
        }

    }

    private fun bindNote(note: Note) {
        binding.apply {
            titleText.text = note.title
            contentText.text = note.content
        }
        if (note.checked != null) {
            binding.isDone.visibility = View.VISIBLE
            binding.isDone.isChecked = note.checked
            binding.isDone.setOnCheckedChangeListener { button, b ->
                viewModel.updateNote(note.copy(checked = b))
            }
        } else {
            binding.isDone.visibility = View.GONE
        }
        binding.deleteButton.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(requireContext())
            builder.setMessage(getString(R.string.question))// "Do you want to delete it?"
                .setPositiveButton(getString(R.string.yes)) { dialogInterface, i ->
                    viewModel.deleteNote(note)
                    findNavController().popBackStack()
                }
                .setNegativeButton(getString(R.string.no)) { dialogInterface, i -> }
            builder.create().show() // чтобы работало

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