package com.example.shoesshop.ui.viewModel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.ForgotPasswordRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class ForgotPasswordViewModel : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    val isEmailValid = MutableStateFlow(false)

    private val _state =
        MutableStateFlow<PasswordRecoveryState>(PasswordRecoveryState.Idle)
    val state: StateFlow<PasswordRecoveryState> = _state

    fun updateEmail(value: String) {
        _email.value = value
        isEmailValid.value =
            value.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }

    fun recoverPassword() {
        if (!isEmailValid.value) {
            _state.value = PasswordRecoveryState.Error("Введите корректный email")
            return
        }

        viewModelScope.launch {
            _state.value = PasswordRecoveryState.Loading
            try {
                val response = RetrofitInstance.userManagementService
                    .recoverPassword(ForgotPasswordRequest(email.value))

                if (response.isSuccessful) {
                    _state.value = PasswordRecoveryState.Success(
                        "Письмо для сброса пароля отправлено на ${email.value}"
                    )
                } else {
                    _state.value = PasswordRecoveryState.Error(
                        "Ошибка: ${response.code()} ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                val msg = when (e) {
                    is ConnectException -> "Нет подключения к интернету"
                    is SocketTimeoutException -> "Превышено время ожидания"
                    else -> e.message ?: "Неизвестная ошибка"
                }
                _state.value = PasswordRecoveryState.Error(msg)
            }
        }
    }

    fun resetState() {
        _state.value = PasswordRecoveryState.Idle
    }
}

sealed class PasswordRecoveryState {
    object Idle : PasswordRecoveryState()
    object Loading : PasswordRecoveryState()
    data class Success(val message: String) : PasswordRecoveryState()
    data class Error(val message: String) : PasswordRecoveryState()
}
