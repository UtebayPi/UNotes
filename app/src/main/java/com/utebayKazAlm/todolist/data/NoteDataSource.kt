package com.utebayKazAlm.todolist.data

import android.util.Log
import com.utebayKazAlm.todolist.viewmodel.VIEW_MODEL
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

const val DATA_SOURCE = "DataSource"

class NoteDataSource @Inject constructor(val dao: NoteDao) {

    val notes = dao.getNotes()

    fun getNote(id: Int) = dao.getNote(id)

    suspend fun addNote(note: Note) {
        try {
            dao.insert(note)
        } catch (e: Exception) {
            Log.d(DATA_SOURCE, "Exception caught: $e")
        }
    }

    suspend fun updateNote(note: Note) {
        try {
            dao.update(note)
        } catch (e: Exception) {
            Log.d(VIEW_MODEL, "Exception caught: $e")
        }
    }

    suspend fun deleteNote(note: Note) {
        try {
            dao.delete(note)
        } catch (e: Exception) {
            Log.d(VIEW_MODEL, "Exception caught: $e")
        }
    }
}