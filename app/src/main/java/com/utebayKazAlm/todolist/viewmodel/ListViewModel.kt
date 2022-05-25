package com.utebayKazAlm.todolist.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.utebayKazAlm.todolist.data.Note
import com.utebayKazAlm.todolist.data.NoteDao
import com.utebayKazAlm.todolist.data.NoteDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val VIEW_MODEL = "ListViewModel"
@HiltViewModel
class ListViewModel @Inject constructor ( val database: NoteDatabase) : ViewModel() {

    private val dao = database.noteDao()

    val notes = dao.getNotes().asLiveData()

    fun getNote(id: Int) = dao.getNote(id).asLiveData()


    fun addNote(note: Note): Boolean {
        if (!note.isValidNote()) return false
        viewModelScope.launch {
            try {
                dao.insert(note)
            } catch (e: Exception) {
                Log.d(VIEW_MODEL, "Exception caught: $e")
            }
        }
        return true
    }

    fun updateNote(note: Note): Boolean {
        if (!note.isValidNote()) return false
        viewModelScope.launch {
            try {
                dao.update(note)
            } catch (e: Exception) {
                Log.d(VIEW_MODEL, "Exception caught: $e")
            }
        }
        return true
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                dao.delete(note)
            } catch (e: Exception) {
                Log.d(VIEW_MODEL, "Exception caught: $e")
            }
        }

    }

//    class Factory(private val dao: NoteDao) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return ListViewModel(dao) as T
//            }
//            throw IllegalArgumentException("Unknown ViewModel class")
//        }
//    }
}

