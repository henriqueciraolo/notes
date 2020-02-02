package br.com.hciraolo.notes

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.hciraolo.notes.notes.repository.dao.NotesDao
import br.com.hciraolo.notes.notes.repository.data.NoteDto
import br.com.hciraolo.notes.notes.repository.data.PriorityTypeConverter

@Database(entities = arrayOf(NoteDto::class), version = 1, exportSchema = false)
@TypeConverters(PriorityTypeConverter::class)
abstract class AppDatabase : RoomDatabase()  {
    abstract fun notesDao(): NotesDao
}