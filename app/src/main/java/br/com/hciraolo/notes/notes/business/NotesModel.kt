package br.com.hciraolo.notes.notes.business

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.hciraolo.notes.notes.presentation.data.*
import br.com.hciraolo.notes.notes.repository.INotesRepository
import br.com.hciraolo.notes.notes.repository.NotesRepository
import br.com.hciraolo.notes.notes.repository.data.NoteDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotesModel private constructor() :
    ListNotesData, NoteFormData {

    companion object {
        val instance = NotesModel()
    }

    private val listNoteStateLiveData = MutableLiveData<ListNoteState>()
    private val noteStateLiveData = MutableLiveData<NoteState>()
    private val notesRepository: INotesRepository = NotesRepository()

    override fun getListNoteStateLiveData(): LiveData<ListNoteState> {
        return listNoteStateLiveData
    }

    override fun getAllNotes() {
        listNoteStateLiveData.value = ListNoteState.LOADING

        GlobalScope.launch(context = Dispatchers.IO) {
            try {
                val notes = notesRepository.getAllNotes()
                val notesPres = ArrayList<ListNote>()
                for (note in notes) {
                    val notePres =
                        ListNote(
                            id = note.id!!,
                            title = note.title!!,
                            shortDescription = note.shortDescription!!,
                            priority = note.priority!!
                        )
                    notesPres.add(notePres)
                }

                val listNoteState = ListNoteState.LIST
                listNoteState.complement =
                    ListNoteComplement(
                        notesPres,
                        null
                    )
                GlobalScope.launch(context = Dispatchers.Main) {
                    listNoteStateLiveData.value = listNoteState
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                val listNoteState = ListNoteState.ERROR
                listNoteState.complement =
                    ListNoteComplement(
                        null,
                        ex
                    )
                GlobalScope.launch(context = Dispatchers.Main) {
                    listNoteStateLiveData.value = listNoteState
                }
            }
        }
    }

    override fun deleteNote(listNote: ListNote) {
        listNoteStateLiveData.value = ListNoteState.LOADING

        GlobalScope.launch(context = Dispatchers.IO) {
            try {
                val noteDto = NoteDto(listNote.id, null, null, null, null)
                notesRepository.deleteNote(noteDto)

                GlobalScope.launch(context = Dispatchers.Main) {
                    listNoteStateLiveData.value = ListNoteState.DELETE
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                val listNoteState = ListNoteState.ERROR
                listNoteState.complement =
                    ListNoteComplement(
                        null,
                        ex
                    )
                GlobalScope.launch(context = Dispatchers.Main) {
                    listNoteStateLiveData.value = listNoteState
                }
            }
        }
    }

    override fun deleteAll() {
        listNoteStateLiveData.value = ListNoteState.LOADING

        GlobalScope.launch(context = Dispatchers.IO) {
            try {
                notesRepository.deleteAll()

                GlobalScope.launch(context = Dispatchers.Main) {
                    listNoteStateLiveData.value = ListNoteState.DELETE_ALL
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                val listNoteState = ListNoteState.ERROR
                listNoteState.complement =
                    ListNoteComplement(
                        null,
                        ex
                    )
                GlobalScope.launch(context = Dispatchers.Main) {
                    listNoteStateLiveData.value = listNoteState
                }
            }
        }
    }

    override fun getNoteStateLiveData(): LiveData<NoteState> {
        return noteStateLiveData
    }

    override fun getNoteById(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertNote(note: Note) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editNote(note: Note) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}