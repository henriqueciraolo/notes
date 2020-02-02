package br.com.hciraolo.notes.login.business

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.hciraolo.notes.AppApplication
import br.com.hciraolo.notes.login.presentation.LoginFormData
import br.com.hciraolo.notes.login.presentation.data.LoginInfoState
import br.com.hciraolo.notes.login.presentation.data.LoginState
import br.com.hciraolo.notes.login.presentation.data.LoginStateError
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth

class LoginModel private constructor() : LoginFormData {
    companion object {
        val instance = LoginModel()
    }

    private val loginStateLiveData = MutableLiveData<LoginState>()
    private val loginInfoStateLiveData = MutableLiveData<LoginInfoState>()

    override fun getLoginStateLiveData(): LiveData<LoginState> {
        return loginStateLiveData
    }

    override fun getLoginInfoStateLiveData(): LiveData<LoginInfoState> {
        return loginInfoStateLiveData
    }

    override fun login(email: String, password: String) {
        loginStateLiveData.postValue(LoginState.LOADING)
        AppApplication.firebaseAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                loginStateLiveData.postValue(LoginState.AUTHENTICATED)
            } else {
                val loginState: LoginState
                if (it.exception is FirebaseNetworkException) {
                    loginState = LoginState.NETWORK
                    loginState.complement = LoginStateError(it.exception)
                } else {
                    loginState = LoginState.ERROR
                    loginState.complement = LoginStateError(it.exception)
                }
                loginState.complement!!.error!!.printStackTrace()
                loginStateLiveData.postValue(loginState)
            }
        }
    }

    override fun saveLogin(username: String, password: String, save: Boolean) {
        if (save) {
            AppApplication.sharedPreferencesRepository!!.putValue("username", username)
            AppApplication.sharedPreferencesRepository!!.putValue("password", password)
        } else {
            AppApplication.sharedPreferencesRepository!!.putValue("username", "")
            AppApplication.sharedPreferencesRepository!!.putValue("password", "")
        }
        AppApplication.sharedPreferencesRepository!!.putValue("save_login", save)
    }

    override fun getLogin() {
        val loginInfoState = LoginInfoState(
            AppApplication.sharedPreferencesRepository!!.getStringValue("username"),
            AppApplication.sharedPreferencesRepository!!.getStringValue("password"),
            AppApplication.sharedPreferencesRepository!!.getBooleanValue("save_login")
        )

        loginInfoStateLiveData.value = loginInfoState
    }
}