package com.example.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todolist.BaseApplication
import com.example.todolist.R
import com.example.todolist.data.Note
import com.example.todolist.databinding.FragmentListBinding
import com.example.todolist.viewmodel.ListViewModel

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListViewModel by activityViewModels {
        ListViewModel.Factory((activity?.application as BaseApplication).database.noteDao())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = NoteListAdapter {
            val action = ListFragmentDirections.actionListFragmentToDetailFragment(id = it.id)
            findNavController().navigate(action)
        }
        binding.list.adapter = adapter
        viewModel.notes.observe(viewLifecycleOwner) {it: List<Note>?->
            adapter.submitList(it)
        }

        binding.addButton.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToAddEditFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}