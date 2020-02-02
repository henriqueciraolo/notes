package br.com.hciraolo.notes.notes.repository

import br.com.hciraolo.notes.AppApplication
import br.com.hciraolo.notes.notes.repository.data.NoteDto

class NotesRepository : INotesRepository {
    override suspend fun getAllNotes() = AppApplication.appDatabase!!.notesDao().getAll()
    override suspend fun getNoteById(id: Int) = AppApplication.appDatabase!!.notesDao().getById(id)
    override suspend fun addNote(noteDto: NoteDto) = AppApplication.appDatabase!!.notesDao().insert(noteDto)
    override suspend fun editNote(noteDto: NoteDto) = AppApplication.appDatabase!!.notesDao().update(noteDto)
    override suspend fun deleteNote(noteDto: NoteDto) = AppApplication.appDatabase!!.notesDao().delete(noteDto)
    override suspend fun deleteAll() = AppApplication.appDatabase!!.notesDao().deleteAll()
}