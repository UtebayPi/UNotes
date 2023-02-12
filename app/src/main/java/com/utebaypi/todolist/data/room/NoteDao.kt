package com.utebaypi.todolist.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)
    @Update
    suspend fun update(note: Note)
    @Delete
    suspend fun delete(note: Note)
    @Query("select * from note where id = :id")
     fun getNote(id: Int): Flow<Note>
    @Query("select * from note order by id desc")
    fun getNotes(): Flow<List<Note>>
}