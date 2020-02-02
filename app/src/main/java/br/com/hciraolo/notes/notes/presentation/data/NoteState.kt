package br.com.hciraolo.notes.notes.presentation.data

enum class NoteState {
    LOADING,
    GETID,
    CREATED,
    EDITED,
    ERROR;

    var complement: NoteComplement? = null
}

data class NoteComplement(val note: Note?, val error: Throwable?)