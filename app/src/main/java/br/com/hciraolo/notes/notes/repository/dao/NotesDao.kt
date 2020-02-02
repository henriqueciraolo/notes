package br.com.hciraolo.notes.notes.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.hciraolo.notes.notes.repository.data.NoteDto

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes")
    suspend fun getAll() : List<NoteDto>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getById(id: Int) : NoteDto

    @Insert
    suspend fun insert(note: NoteDto)

    @Update
    suspend fun update(note: NoteDto)

    @Delete
    suspend fun delete(note: NoteDto)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()
}