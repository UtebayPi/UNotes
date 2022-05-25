package com.utebayKazAlm.todolist.data

import androidx.lifecycle.asLiveData
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

class NoteRepository @Inject constructor(val dataSource: NoteDataSource) {
    val notes = dataSource.notes
    fun getNote(id: Int) = dataSource.getNote(id)
    suspend fun addNote(note: Note):Boolean{
        if (!note.isValidNote()) return false
        dataSource.addNote(note)
        return true
    }
    suspend fun updateNote(note: Note):Boolean{
        if (!note.isValidNote()) return false
        dataSource.updateNote(note)
        return true
    }
    suspend fun deleteNote(note: Note){
        dataSource.deleteNote(note)
    }
}