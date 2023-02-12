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
import com.utebaypi.todolist.R
import com.utebaypi.todolist.data.room.Note
import com.utebaypi.todolist.databinding.FragmentListBinding
import com.utebaypi.todolist.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = NoteListAdapter {
            //When you press on a note from a recyclerView, it goes to a new screen with it's id.
            val action = ListFragmentDirections.actionListFragmentToDetailFragment(id = it.id)
            findNavController().navigate(action)
        }
        binding.list.adapter = adapter

        //Taking data from db and loading into recyclerView
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notes.collectLatest { it: List<Note>? ->
                    adapter.submitList(it)
                }
            }
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