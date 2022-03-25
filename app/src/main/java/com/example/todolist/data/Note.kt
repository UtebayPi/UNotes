package com.example.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val checked: Boolean? = null,
) {
    fun isValidNote(): Boolean {
        return title.isNotBlank() && content.isNotBlank()
    }

    companion object {
        fun getFormattedNote(id: Int = 0, title: String, content: String, checked: Boolean?): Note {
            return Note(
                id = id, title = title.trim(),
                content = content.trim(),
                checked = checked
            )
        }
    }

}