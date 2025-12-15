package com.example.shoesshop.data.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterAccountUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isPolicyAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class RegisterAccountViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterAccountUiState())
    val uiState: StateFlow<RegisterAccountUiState> = _uiState

    fun onNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
    }

    fun onEmailChanged(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun onPolicyChecked(value: Boolean) {
        _uiState.value = _uiState.value.copy(isPolicyAccepted = value)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun register(context: Context, onSuccess: () -> Unit) {
        val state = _uiState.value

        if (!isEmailValid(state.email)) {
            showError("Некорректный email")
            return
        }

        if (state.password.isEmpty()) {
            showError("Пароль не может быть пустым")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true)

            try {
                delay(1500)

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

    private fun isEmailValid(email: String): Boolean {
        val regex = Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]{3,}$")
        return regex.matches(email)
    }
}
