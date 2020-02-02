package br.com.hciraolo.notes.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.hciraolo.notes.R
import br.com.hciraolo.notes.databinding.ActivityLoginBinding
import br.com.hciraolo.notes.extensions.afterTextChanged
import br.com.hciraolo.notes.extensions.launchActivity
import br.com.hciraolo.notes.login.presentation.LoginViewModel
import br.com.hciraolo.notes.login.presentation.data.LoginState
import br.com.hciraolo.notes.notes.presentation.ListNotesActivity

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = loginViewModel


        loginViewModel.getLoginStateLiveData().observe(this, Observer {
            when (it) {
                LoginState.LOADING -> {
                    binding.cnlLoadingLayout.visibility = View.VISIBLE
                }

                LoginState.NETWORK -> {
                    binding.cnlLoadingLayout.visibility = View.GONE

                    AlertDialog.Builder(this)
                        .setTitle(R.string.error_failed_title)
                        .setMessage(R.string.login_failed_fail_network)
                        .setNeutralButton(R.string.action_ok, null)
                        .create()
                        .show()
                }

                LoginState.ERROR -> {
                    binding.cnlLoadingLayout.visibility = View.GONE

                    AlertDialog.Builder(this)
                        .setTitle(R.string.error_failed_title)
                        .setMessage(R.string.login_failed_fail_auth)
                        .setNeutralButton(R.string.action_ok, null)
                        .create()
                        .show()
                }

                LoginState.AUTHENTICATED -> {
                    loginViewModel.saveLogin(binding.tietUsername.text.toString(), binding.tietPassword.text.toString(), binding.swtSaveLogin.isChecked)

                    binding.cnlLoadingLayout.visibility = View.GONE
                    launchActivity<ListNotesActivity> {
                        this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                    finish()
                }
            }
        } )

        loginViewModel.getLoginInfoStateLiveData().observe(this, Observer {
            if (it.saveLogin) {
                binding.tietUsername.setText(it.username)
                binding.tietPassword.setText(it.password)
            }
            binding.swtSaveLogin.isChecked = it.saveLogin
        })

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

        loginViewModel.getLoginData()
    }

    override fun onDestroy() {
        super.onDestroy()
        (loginViewModel.getLoginStateLiveData() as MutableLiveData).value = null
    }
}
