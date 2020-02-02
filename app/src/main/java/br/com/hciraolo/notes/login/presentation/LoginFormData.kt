package br.com.hciraolo.notes.login.presentation

import androidx.lifecycle.LiveData
import br.com.hciraolo.notes.login.presentation.data.LoginState

interface LoginFormData {
    fun getLoginStateLiveData() : LiveData<LoginState>
    fun login(email: String, password: String)
}