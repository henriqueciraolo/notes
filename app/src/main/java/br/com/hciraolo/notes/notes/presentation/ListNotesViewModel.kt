package br.com.hciraolo.notes.notes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.hciraolo.notes.ModelManager
import br.com.hciraolo.notes.notes.business.NotesModel
import br.com.hciraolo.notes.notes.presentation.data.ListNote
import br.com.hciraolo.notes.notes.presentation.data.ListNotesData
import br.com.hciraolo.notes.notes.presentation.data.ListNoteState

class ListNotesViewModel : ViewModel() {

    val notes: ListNotesData = ModelManager().getListNotesData()

    fun getAllNotes() {
        notes.getAllNotes()
    }

    fun deleteNote(note: ListNote) {
        notes.deleteNote(note)
    }

    fun deleteAll() {
        notes.deleteAll()
    }

    fun getListNoteStateLiveData() : LiveData<ListNoteState> {
        return notes.getListNoteStateLiveData()
    }
}