package com.utebaypi.todolist.data

import android.util.Log
import com.utebaypi.todolist.data.room.Note
import com.utebaypi.todolist.data.room.NoteDao


class NoteRepository(val dao: NoteDao) {

    val REPO = "NoteRepository"

    val notes = dao.getNotes()

    fun getNote(id: Int) = dao.getNote(id)

    suspend fun addNote(note: Note) {
        try {
            dao.insert(note)
        } catch (e: Exception) {
            Log.d(REPO, "Exception caught: $e")
        }
    }

    suspend fun updateNote(note: Note) {
        try {
            dao.update(note)
        } catch (e: Exception) {
            Log.d(REPO, "Exception caught: $e")
        }
    }

    suspend fun deleteNote(note: Note) {
        try {
            dao.delete(note)
        } catch (e: Exception) {
            Log.d(REPO, "Exception caught: $e")
        }
    }
}