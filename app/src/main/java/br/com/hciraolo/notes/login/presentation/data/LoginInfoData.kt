package br.com.hciraolo.notes.login.presentation.data

import com.google.firebase.auth.FirebaseUser

data class LoginInfoState(val username: String, val password: String, val saveLogin: Boolean)