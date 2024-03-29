package com.utebaypi.todolist.di

import android.content.Context
import androidx.room.Room
import com.utebaypi.todolist.data.room.NoteDatabase
import com.utebaypi.todolist.data.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
            "note_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRepository(database: NoteDatabase): NoteRepository = NoteRepository(database.noteDao())
}