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


    val notes = repository.notes

    fun getNote(id: Int) = repository.getNote(id)


    fun addNote(note: Note): Boolean {
        //Проверяем на валидность данных.
        if (!note.isValidNote()) return false
        viewModelScope.launch {
            repository.addNote(note)
        }
        return true
    }

    fun updateNote(note: Note): Boolean {
        if (!note.isValidNote()) return false
        viewModelScope.launch {
            repository.updateNote(note)
        }
        return true
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }

    }
}

