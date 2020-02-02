package br.com.hciraolo.notes.notes.presentation.data

import androidx.lifecycle.LiveData

interface NoteFormData {
    fun getNoteStateLiveData() : LiveData<NoteState>
    fun getNoteById(id: Int)
    fun insertNote(note: Note)
    fun editNote(note: Note)
}