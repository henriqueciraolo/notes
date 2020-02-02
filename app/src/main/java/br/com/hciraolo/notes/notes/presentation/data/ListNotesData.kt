package br.com.hciraolo.notes.notes.presentation.data

import androidx.lifecycle.LiveData

interface ListNotesData {
    fun getListNoteStateLiveData() : LiveData<ListNoteState>
    fun getAllNotes()
    fun deleteNote(listNote: ListNote)
    fun deleteAll()
}