package com.example.shoesshop.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.SignInRequest
import com.example.shoesshop.data.models.SignInResponse
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
        this.email = email
        this.password = password

        if (email.isEmpty()) {
            val error = "Email не может быть пустым"
            showError(error)
            onError(error)
            return
        }

        val emailError = getEmailError(email)
        if (emailError != null) {
            showError(emailError)
            onError(emailError)
            return
        }

        if (password.isEmpty()) {
            val error = "Пароль не может быть пустым"
            showError(error)
            onError(error)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val signInData = SignInRequest(email, password)
                Log.d("SignIn", "Отправляем запрос: $signInData")

                val response = RetrofitInstance.userManagementService.signIn(signInData)

                Log.d(
                    "SignIn",
                    "Ответ: code=${response.code()} body=${response.body()} errBody=${response.errorBody()?.string()}"
                )

                if (response.isSuccessful) {
                    val signInResponse: SignInResponse? = response.body()
                    val user = signInResponse?.user

                    if (user != null && user.id != null && user.email != null) {
                        val userId = user.id
                        val userEmail = user.email

                        Log.v(
                            "SignIn",
                            "Пользователь авторизован: ID=$userId, email=$userEmail"
                        )

                        saveUserData(
                            context = context,
                            userId = userId,
                            email = userEmail
                        )

                        onSuccess()
                    } else {
                        val error = "Пустой или некорректный ответ сервера"
                        showError(error)
                        onError(error)
                    }
                } else {
                    Log.e("SignIn", "HTTP ошибка: ${response.code()} - ${response.message()}")
                    val errorMessage = when (response.code()) {
                        400 -> "Неверный email или пароль"
                        422 -> "Некорректные данные"
                        500 -> "Ошибка сервера"
                        else -> "Ошибка входа: ${response.code()}"
                    }
                    showError(errorMessage)
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                val msg = e.message ?: "Неизвестная ошибка"
                val errorMessage = "Ошибка сети. Проверьте подключение\n$msg"
                Log.e("SignIn", "Сетевая ошибка: $msg", e)
                showError(errorMessage)
                onError(errorMessage)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    // ---------- сохранение пользователя ----------
    private fun saveUserData(context: Context, userId: String, email: String) {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secure_user_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            with(sharedPreferences.edit()) {
                putString("user_id", userId)
                putString("user_email", email)
                putBoolean("is_logged_in", true)
                putLong("login_time", System.currentTimeMillis())
                apply()
            }

            Log.d("SignInViewModel", "Данные сохранены: ID=$userId, email=$email")
        } catch (e: Exception) {
            Log.e("SignInViewModel", "Ошибка сохранения данных, fallback", e)
            fallbackSaveUserData(context, userId, email)
        }
    }

    private fun fallbackSaveUserData(context: Context, userId: String, email: String) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("user_id", userId)
            putString("user_email", email)
            putBoolean("is_logged_in", true)
            putLong("login_time", System.currentTimeMillis())
            apply()
        }
        Log.d("SignInViewModel", "Fallback сохранение: ID=$userId")
    }

    // ---------- ошибки и валидация ----------
    private fun showError(message: String) {
        _uiState.value = _uiState.value.copy(errorMessage = message)
    }

    private fun getEmailError(email: String): String? {
        return when {
            email.isEmpty() -> "Email не может быть пустым"
            email.trim() != email -> "Email не должен содержать пробелы"
            !email.contains("@") -> "Email должен содержать символ @"
            else -> {
                val parts = email.split("@")
                if (parts.size != 2) return "Некорректный формат email"

                val localPart = parts[0]
                val domainPart = parts[1]

                if (localPart.isEmpty()) return "Имя пользователя не может быть пустым"
                if (localPart.startsWith(".") || localPart.endsWith("."))
                    return "Имя пользователя не может начинаться/заканчиваться точкой"
                if (localPart.contains(".."))
                    return "Имя пользователя не может содержать две точки подряд"
                if (localPart.length > 64)
                    return "Имя пользователя слишком длинное (макс. 64 символа)"

                if (domainPart.isEmpty()) return "Доменная часть не может быть пустой"
                if (!domainPart.contains(".")) return "Доменное имя должно содержать точку"

                val domainParts = domainPart.split(".")
                if (domainParts.size < 2) return "Некорректный формат домена"
                if (domainParts.last().length < 2) return "Домен должен содержать минимум 2 символа"

                if (!EMAIL_PATTERN.matcher(email).matches()) {
                    return "Некорректный формат email. Пример: example@domain.com"
                }
                null
            }
        }
    }
}
