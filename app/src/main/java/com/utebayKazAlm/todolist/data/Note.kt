package com.utebayKazAlm.todolist.data

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
}