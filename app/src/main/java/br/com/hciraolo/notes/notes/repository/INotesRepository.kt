package br.com.hciraolo.notes.notes.repository

import androidx.lifecycle.LiveData
import br.com.hciraolo.notes.notes.repository.data.NoteDto

interface INotesRepository {
    suspend fun getAllNotes() : List<NoteDto>
    suspend fun getNoteById(id: Int) : NoteDto
    suspend fun addNote(noteDto: NoteDto)
    suspend fun editNote(noteDto: NoteDto)
    suspend fun deleteNote(noteDto: NoteDto)
    suspend fun deleteAll()
}