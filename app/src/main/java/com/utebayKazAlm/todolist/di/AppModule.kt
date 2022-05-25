package com.utebayKazAlm.todolist.di

import android.content.Context
import androidx.room.Room
import com.utebayKazAlm.todolist.data.NoteDataSource
import com.utebayKazAlm.todolist.data.room.NoteDatabase
import com.utebayKazAlm.todolist.data.NoteRepository
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
    fun provideRepository(dataSource: NoteDataSource): NoteRepository = NoteRepository(dataSource)

    @Provides
    @Singleton
    fun provideDataSource(database: NoteDatabase): NoteDataSource = NoteDataSource(database.noteDao())
}