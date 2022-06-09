package com.utebayKazAlm.todolist.ui


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
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.utebayKazAlm.todolist.R
import com.utebayKazAlm.todolist.data.room.Note
import com.utebayKazAlm.todolist.databinding.FragmentDetailBinding
import com.utebayKazAlm.todolist.viewmodel.ListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
                    //Эта проверка нужно чтобы избежать ошибок. LiveData даже если указано что не может
                    //возвращять null используя null safety, если запись удалится в базе данных, возвращяет null.
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
        //Проверяем какого типа записка. Классификаций написал в Note. Если это задача (checked != null) то
        //делаем кнопку изменения статуса задачи видимой, checkbox даем значение checked, и при
        // его нажатий, меняем checked в note
        if (note.checked != null) {
            binding.isDone.visibility = View.VISIBLE
            binding.isDone.isChecked = note.checked
            binding.isDone.setOnCheckedChangeListener { button, b ->
                viewModel.viewModelScope.launch {
                    viewModel.updateNote(note.copy(checked = b))
                }
            }
        } else { // Если просто записка (checked = null), то скрываем кнопку
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