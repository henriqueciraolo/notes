package br.com.hciraolo.notes.login.presentation.data

import com.google.firebase.auth.FirebaseUser

enum class LoginState {
    LOADING,
    AUTHENTICATED,
    NETWORK,
    ERROR,
    ;

    var complement: LoginStateError? = null

}

data class LoginStateError(val error: Throwable?)