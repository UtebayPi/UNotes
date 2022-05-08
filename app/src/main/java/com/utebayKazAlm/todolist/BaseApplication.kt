package com.utebayKazAlm.todolist

import android.app.Application
import com.utebayKazAlm.todolist.data.NoteDatabase

class BaseApplication: Application() {
    val database: NoteDatabase by lazy {
        NoteDatabase.getDatabase(this)
    }
}