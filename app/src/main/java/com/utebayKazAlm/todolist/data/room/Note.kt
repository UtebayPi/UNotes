package com.utebayKazAlm.todolist.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    // This value signifies if this note is a task. If it's not null, then it is a task.
    // True or false means if the task is unfinished or done.
    val checked: Boolean? = null,
) {
    //Validating content
    fun isValidNote(): Boolean {
        return title.isNotBlank() && content.isNotBlank()
    }
}