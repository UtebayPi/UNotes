package com.utebayKazAlm.todolist.viewmodel

import androidx.lifecycle.*
import com.utebayKazAlm.todolist.data.room.Note
import com.utebayKazAlm.todolist.data.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val VIEW_MODEL = "ListViewModel"

@HiltViewModel
class ListViewModel @Inject constructor(val repository: NoteRepository) : ViewModel() {


    val notes = repository.notes.asLiveData()

    fun getNote(id: Int) = repository.getNote(id).asLiveData()


    suspend fun addNote(note: Note): Boolean {
        return repository.addNote(note)
    }

    suspend fun updateNote(note: Note): Boolean {
        return repository.updateNote(note)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }

    }
}

