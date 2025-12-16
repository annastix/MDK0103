package com.example.shoesshop.data.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesshop.data.models.RegisterRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

data class SignInUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class SignInViewModel : ViewModel() {
    var email: String = ""
    var password: String = ""
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState

    // Регулярное выражение для проверки email по стандарту RFC 5322
    private val EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    )

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun register(
        context: Context,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isEmpty()) {
            showError("Email не может быть пустым")
            return
        }

        // Проверяем email
        val emailError = getEmailError(email)
        if (emailError != null) {
            showError(emailError)
            return
        }

        if (password.isEmpty()) {
            showError("Пароль не может быть пустым")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
//                val signInData = SignInRequest(email, password)
//                val response = RetrofitInstance.userManagementService.signIn(signInData)
//                if (response.isSuccessful){
//                    response.body()?.let {
//                        Log.v("SignIn", "Пользователь успешно авторизован: ${it.email}")
//                        onSuccess()
//                    }
//                }
//                else {
//                    Log.e("SignIn", "HTTP ошибка: ${response.code()} - ${response.message()}")
//                    val errorMessage = when
//                                               (response.code())
//                    {
//                        400 -> "Неверный email или пароль"
//                        422 -> "Некорректные данные"
//                        500 -> "Ошибка сервера"
//                        else -> "Ошибка входа: ${response.message()}"
//                    }
//                    val errorBody = response.errorBody()?.string()
//                    Log.e("SignIn", "Тело ошибки: $errorBody")
//                    onError(errorMessage)
//                }
            } catch (e: Exception) {
                showError("Ошибка сети. Проверьте подключение к Интернету\n"+e.message.toString())
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun saveCredentialsToSharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_email", email)
        editor.putString("user_password", password)
        editor.putBoolean("is_user_verified", false) // пока не верифицирован
        editor.apply()
        Log.d("SignUpViewModel", "Credentials saved to SharedPreferences: $email")
    }

    private fun showError(message: String) {
        _uiState.value = _uiState.value.copy(errorMessage = message)
    }

    private fun getEmailError(email: String): String? {
        return when {
            email.isEmpty() -> "Email не может быть пустым"
            email.trim() != email -> "Email не должен содержать пробелы в начале или конце"
            !email.contains("@") -> "Email должен содержать символ @"
            else -> {
                val parts = email.split("@")
                if (parts.size != 2) return "Некорректный формат email"

                val localPart = parts[0]
                val domainPart = parts[1]

                // Проверка локальной части (до @)
                if (localPart.isEmpty()) return "Имя пользователя не может быть пустым"
                if (localPart.startsWith(".") || localPart.endsWith("."))
                    return "Имя пользователя не может начинаться или заканчиваться точкой"
                if (localPart.contains(".."))
                    return "Имя пользователя не может содержать две точки подряд"
                if (localPart.length > 64)
                    return "Имя пользователя слишком длинное (максимум 64 символа)"

                // Проверка доменной части
                if (domainPart.isEmpty()) return "Доменная часть не может быть пустой"
                if (!domainPart.contains(".")) return "Доменное имя должно содержать точку"

                val domainParts = domainPart.split(".")
                if (domainParts.size < 2) return "Некорректный формат домена"

                // Проверка TLD (последней части)
                val tld = domainParts.last()
                if (tld.length < 2) return "Старший домен должен содержать минимум 2 символа"

                // Основная проверка через регулярное выражение
                if (!EMAIL_PATTERN.matcher(email).matches()) {
                    return "Некорректный формат email. Пример: example@domain.com"
                }

                null
            }
        }
    }
}