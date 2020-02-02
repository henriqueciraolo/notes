package br.com.hciraolo.notes.login.business

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.hciraolo.notes.AppApplication
import br.com.hciraolo.notes.login.presentation.LoginFormData
import br.com.hciraolo.notes.login.presentation.data.LoginState
import br.com.hciraolo.notes.login.presentation.data.LoginStateError
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth

class LoginModel : LoginFormData {
    companion object {
        val instance = LoginModel()
    }

    private constructor()

    private val loginStateLiveData = MutableLiveData<LoginState>()

    override fun getLoginStateLiveData(): LiveData<LoginState> {
        return loginStateLiveData
    }

    override fun login(email: String, password: String) {
        loginStateLiveData.postValue(LoginState.LOADING)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
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


}