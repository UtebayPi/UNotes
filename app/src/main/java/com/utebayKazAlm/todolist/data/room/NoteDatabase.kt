package com.utebayKazAlm.todolist.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.utebayKazAlm.todolist.data.room.Note
import com.utebayKazAlm.todolist.data.room.NoteDao

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}