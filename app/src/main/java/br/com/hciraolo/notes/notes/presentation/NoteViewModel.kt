package br.com.hciraolo.notes.notes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.hciraolo.notes.ModelManager
import br.com.hciraolo.notes.notes.business.NotesModel
import br.com.hciraolo.notes.notes.presentation.data.Note
import br.com.hciraolo.notes.notes.presentation.data.NoteFormData
import br.com.hciraolo.notes.notes.presentation.data.NoteFormState
import br.com.hciraolo.notes.notes.presentation.data.NoteState
import br.com.hciraolo.notes.notes.repository.data.Priority

class NoteViewModel : ViewModel() {

    private val _noteForm = MutableLiveData<NoteFormState>()
    val noteFormState: LiveData<NoteFormState> = _noteForm

    val notes: NoteFormData = ModelManager().getNoteFormData()

    fun getNoteById(id: Int) {
        notes.getNoteById(id)
    }

    fun addNote(title: String, shortDescription: String, description: String, priority: Priority) {
        notes.insertNote(Note(id = null, title = title, shortDescription = shortDescription, description = description, priority = priority))
    }

    fun editNote(id: Int, title: String, shortDescription: String, description: String, priority: Priority) {
        notes.editNote(Note(id = id, title = title, shortDescription = shortDescription, description = description, priority = priority))
    }

    fun getListNoteStateLiveData() : LiveData<NoteState> {
        return notes.getNoteStateLiveData()
    }
}