package com.utebayKazAlm.todolist.data

import android.util.Log
import com.utebayKazAlm.todolist.data.room.Note
import com.utebayKazAlm.todolist.data.room.NoteDao

const val DATA_SOURCE = "DataSource"
class NoteDataSource (val dao: NoteDao) {

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
            Log.d(DATA_SOURCE, "Exception caught: $e")
        }
    }

    suspend fun deleteNote(note: Note) {
        try {
            dao.delete(note)
        } catch (e: Exception) {
            Log.d(DATA_SOURCE, "Exception caught: $e")
        }
    }
}