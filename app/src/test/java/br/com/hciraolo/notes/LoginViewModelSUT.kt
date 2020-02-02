package br.com.hciraolo.notes

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import br.com.hciraolo.notes.login.presentation.LoginFormData
import br.com.hciraolo.notes.login.presentation.LoginViewModel
import br.com.hciraolo.notes.login.presentation.data.LoginFormState
import br.com.hciraolo.notes.login.presentation.data.LoginState
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.regex.Pattern

class LoginViewModelSUT {

    @MockK
    lateinit var login: LoginFormData

    @MockK
    lateinit var _loginForm: MutableLiveData<LoginFormState>

    @InjectMockKs(overrideValues = true, injectImmutable = true)
    lateinit var SUT: LoginViewModel

    @Before
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

    @Test
    fun login_call() {
        SUT.login("teste@teste.com", "123456")
        verify { login.login("teste@teste.com", "123456") }
    }

    @Test
    fun getLoginStateLiveData_call() {
        every { login.getLoginStateLiveData() } returns MutableLiveData<LoginState>()
        SUT.getLoginStateLiveData()
        verify { login.getLoginStateLiveData() }
    }

    @Test
    fun loginDataChanged_verifySuccess() {
        val slot = slot<LoginFormState>()

        every { _loginForm.value = capture(slot) } just Runs
        mockPatternsEmailAddress()
        SUT.loginDataChanged("teste@teste.com", "123456")

        assert(slot.captured.isDataValid)
    }

    @Test
    fun loginDataChanged_emailIsInvalid() {
        val slot = slot<LoginFormState>()

        every { _loginForm.value = capture(slot) } just Runs
        mockPatternsEmailAddress()
        SUT.loginDataChanged("teste", "123456")

        assert(slot.captured.usernameError == R.string.invalid_username)
    }

    @Test
    fun loginDataChanged_passwordlIsInvalid() {
        val slot = slot<LoginFormState>()

        every { _loginForm.value = capture(slot) } just Runs
        mockPatternsEmailAddress()
        SUT.loginDataChanged("teste@teste.com", "123")

        assert(slot.captured.passwordError == R.string.invalid_password)
    }

    private fun mockPatternsEmailAddress() {
        val sdkIntField = Patterns::class.java.getField("EMAIL_ADDRESS")
        sdkIntField.isAccessible = true
        Field::class.java.getDeclaredField("modifiers").also {
            it.isAccessible = true
            it.set(sdkIntField, sdkIntField.modifiers and Modifier.FINAL.inv())
        }
        sdkIntField.set(null, Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        ))
    }
}