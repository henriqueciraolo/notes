package br.com.hciraolo.notes

import br.com.hciraolo.notes.login.business.LoginModel
import br.com.hciraolo.notes.login.presentation.LoginFormData
import br.com.hciraolo.notes.notes.business.NotesModel
import br.com.hciraolo.notes.notes.presentation.data.ListNotesData
import br.com.hciraolo.notes.notes.presentation.data.NoteFormData

interface IModelManager {
    fun getLoginFormData() : LoginFormData
    fun getListNotesData() : ListNotesData
    fun getNoteFormData() : NoteFormData
}

class ModelManager : IModelManager{
    companion object {
        private val loginModel = LoginModel()
        private val notesModel = NotesModel()
    }
    override fun getLoginFormData() : LoginFormData = loginModel
    override fun getListNotesData() : ListNotesData = notesModel
    override fun getNoteFormData() : NoteFormData = notesModel
}