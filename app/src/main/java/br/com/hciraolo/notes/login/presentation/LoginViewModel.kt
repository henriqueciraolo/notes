package br.com.hciraolo.notes.login.presentation

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.hciraolo.notes.R
import br.com.hciraolo.notes.login.business.LoginModel
import br.com.hciraolo.notes.login.presentation.data.LoginFormState
import br.com.hciraolo.notes.login.presentation.data.LoginState
import kotlin.math.log

class LoginViewModel : ViewModel() {


    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    val login: LoginFormData
    init {
        login = LoginModel.instance
    }

    fun login(email: String, password: String) {
        login.login(email, password)
    }

    fun getLoginStateLiveData() : LiveData<LoginState> {
        return login.getLoginStateLiveData()
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}