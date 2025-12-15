package com.example.shoesshop.data.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

data class RegisterAccountUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class RegisterAccountViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterAccountUiState())
    val uiState: StateFlow<RegisterAccountUiState> = _uiState

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
        name: String,
        email: String,
        password: String,
        isPolicyAccepted: Boolean,
        onSuccess: () -> Unit
    ) {
        // Сначала проверяем обязательные поля
        if (name.isEmpty()) {
            showError("Имя не может быть пустым")
            return
        }

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

        if (!isPolicyAccepted) {
            showError("Необходимо согласиться с политикой конфиденциальности")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                delay(1500) // Имитация сетевого запроса
                onSuccess()
            } catch (e: Exception) {
                showError("Ошибка сети. Проверьте подключение к Интернету")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
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