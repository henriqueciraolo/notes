package br.com.hciraolo.notes.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.hciraolo.notes.R
import br.com.hciraolo.notes.databinding.ActivityLoginBinding
import br.com.hciraolo.notes.extensions.afterTextChanged
import br.com.hciraolo.notes.login.presentation.LoginViewModel
import br.com.hciraolo.notes.login.presentation.data.LoginState

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        loginViewModel.getLoginStateLiveData().observe(this, Observer { loginState ->
            when (loginState) {
                LoginState.LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.btLogin.isEnabled = false
                }

                LoginState.NETWORK -> {
                    binding.pbLoading.visibility = View.INVISIBLE
                    binding.btLogin.isEnabled = true

                    AlertDialog.Builder(this)
                        .setTitle(R.string.login_failed_title)
                        .setMessage(R.string.login_failed_fail_network)
                        .setNeutralButton(R.string.action_ok, null)
                        .create()
                        .show()
                }

                LoginState.ERROR -> {
                    binding.pbLoading.visibility = View.INVISIBLE
                    binding.btLogin.isEnabled = true

                    AlertDialog.Builder(this)
                        .setTitle(R.string.login_failed_title)
                        .setMessage(R.string.login_failed_fail_auth)
                        .setNeutralButton(R.string.action_ok, null)
                        .create()
                        .show()
                }

                LoginState.AUTHENTICATED -> {
                    binding.pbLoading.visibility = View.INVISIBLE
                    binding.btLogin.isEnabled = true
                    finish()
                }
            }
        } )

        loginViewModel.loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer

            binding.btLogin.isEnabled = loginState.isDataValid


            if (loginState.usernameError != null) {
                binding.tilUsername.error = getString(loginState.usernameError)
            } else {
                binding.tilUsername.error = ""
            }
            if (loginState.passwordError != null) {
                binding.tilPassword.error = getString(loginState.passwordError)
            } else {
                binding.tilPassword.error = ""
            }
        })

        binding.tietUsername.afterTextChanged {
            loginViewModel.loginDataChanged(
                binding.tietUsername.text.toString(),
                binding.tietPassword.text.toString()
            )
        }

        binding.tietPassword.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    binding.tietUsername.text.toString(),
                    binding.tietPassword.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            binding.tietUsername.text.toString(),
                            binding.tietPassword.text.toString()
                        )
                }
                false
            }

            binding.btLogin.setOnClickListener {
                loginViewModel.login(binding.tietUsername.text.toString(), binding.tietPassword.text.toString())
            }
        }
    }
}
