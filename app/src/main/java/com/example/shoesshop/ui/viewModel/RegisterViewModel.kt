package com.example.shoesshop.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern

data class RegisterAccountUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class RegisterAccountViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterAccountUiState())
    val uiState: StateFlow<RegisterAccountUiState> = _uiState

    private val EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    )

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun register(
        context: Context,
        name: String,
        email: String,
        password: String,
        isPolicyAccepted: Boolean,
        onSuccess: () -> Unit
    ) {
        val validationError = validateFields(name, email, password, isPolicyAccepted)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val signUpData = RegisterRequest(email, password)
                val response = RetrofitInstance.userManagementService.signUp(signUpData)

                Log.d("RegisterViewModel", "Response code: ${response.code()}")
                Log.d("RegisterViewModel", "Response message: ${response.message()}")
                Log.d("RegisterViewModel", "Response body: ${response.body()}")
                Log.d(
                    "RegisterViewModel",
                    "Response error body: ${response.errorBody()?.string()}"
                )

                if (response.isSuccessful && response.body() != null) {
                    // userId есть в response.body()?.id, если понадобится
                    saveCredentialsToSharedPreferences(context, name, email, password)
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = when (response.code()) {
                        422 -> {
                            if (errorBody?.contains("already exists", ignoreCase = true) == true) {
                                "Пользователь с таким email уже существует"
                            } else {
                                "Пользователь с таким email уже существует или неверные данные"
                            }
                        }
                        500 -> "Ошибка сервера"
                        else -> "Ошибка регистрации (код: ${response.code()})"
                    }
                    _uiState.update { it.copy(errorMessage = errorMessage) }
                    Log.e("RegisterViewModel", "Error: $errorMessage, Body: $errorBody")
                }
            } catch (e: Exception) {
                val error = "Ошибка сети: ${e.message ?: "Проверьте подключение"}"
                _uiState.update { it.copy(errorMessage = error) }
                Log.e("RegisterViewModel", "Exception: $e")
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun validateFields(
        name: String,
        email: String,
        password: String,
        isPolicyAccepted: Boolean
    ): String? {
        return when {
            name.isEmpty() -> "Имя не может быть пустым"
            email.isEmpty() -> "Email не может быть пустым"
            !EMAIL_PATTERN.matcher(email).matches() -> "Некорректный формат email"
            password.isEmpty() -> "Пароль не может быть пустым"
            !isPolicyAccepted -> "Необходимо согласиться с политикой конфиденциальности"
            else -> null
        }
    }

    private fun saveCredentialsToSharedPreferences(
        context: Context,
        name: String,
        email: String,
        password: String
    ) {
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            .edit()
            .putString("user_name", name)
            .putString("user_email", email)
            .putString("user_password", password)
            .putBoolean("is_user_verified", false)
            .apply()
    }
}
