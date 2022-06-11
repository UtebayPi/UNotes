package com.utebayKazAlm.todolist.data

import android.util.Log
import com.utebayKazAlm.todolist.data.room.Note
import com.utebayKazAlm.todolist.data.room.NoteDao


class NoteRepository(val dao: NoteDao) {
    //Не стал писать dataSource так как приложение слишком маленькое, и это бы добавило ненужного кода

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