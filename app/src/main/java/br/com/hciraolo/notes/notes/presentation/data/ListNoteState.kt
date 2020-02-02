package br.com.hciraolo.notes.notes.presentation.data

enum class ListNoteState {
    LOADING,
    LIST,
    DELETE,
    DELETE_ALL,
    ERROR
    ;

    var complement: ListNoteComplement? = null
}

data class ListNoteComplement(val data: List<ListNote>?, val error: Throwable?)