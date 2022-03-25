package com.example.todolist

import android.app.Application
import com.example.todolist.data.NoteDatabase

class BaseApplication: Application() {
    val database: NoteDatabase by lazy {
        NoteDatabase.getDatabase(this)
    }
}