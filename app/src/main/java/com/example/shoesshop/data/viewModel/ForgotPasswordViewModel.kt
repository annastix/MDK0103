package com.example.shoesshop.data.viewModel

import android.util.Log
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

    private val _passwordRecoveryState =
        MutableStateFlow<PasswordRecoveryState>(PasswordRecoveryState.Idle)
    val passwordRecoveryState: StateFlow<PasswordRecoveryState> = _passwordRecoveryState

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    val isEmailValid = MutableStateFlow(false)

    fun updateEmail(email: String) {
        _email.value = email
        validateEmail(email)
    }

    private fun validateEmail(email: String) {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
        isEmailValid.value = email.matches(emailPattern)
    }

    fun recoverPassword() {
        if (!isEmailValid.value) {
            _passwordRecoveryState.value =
                PasswordRecoveryState.Error("Please enter a valid email address")
            return
        }

        viewModelScope.launch {
            _passwordRecoveryState.value = PasswordRecoveryState.Loading

            try {
                Log.d(
                    "ForgotPasswordViewModel",
                    "Sending recovery request for email: ${_email.value}"
                )

                val request = ForgotPasswordRequest(email = _email.value)

                val response =
                    RetrofitInstance.userManagementService.recoverPassword(request)

                Log.d(
                    "ForgotPasswordViewModel",
                    "Response received: ${response.isSuccessful}"
                )

                if (response.isSuccessful) {
                    // Supabase возвращает 200 OK даже если email не найден[web:104]
                    _passwordRecoveryState.value = PasswordRecoveryState.Success(
                        "Password reset email has been sent to ${_email.value}"
                    )
                } else {
                    val errorCode = response.code()
                    val errorMessage = response.message()
                    val errorBody = response.errorBody()?.string() ?: ""

                    Log.e(
                        "ForgotPasswordViewModel",
                        "Error: $errorCode - $errorMessage - $errorBody"
                    )

                    _passwordRecoveryState.value = PasswordRecoveryState.Error(
                        parseRecoveryError(errorCode, errorMessage, errorBody)
                    )
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is ConnectException -> "No internet connection. Please check your network."
                    is SocketTimeoutException -> "Connection timeout. Please try again."
                    else -> {
                        Log.e("ForgotPasswordViewModel", "Exception: ${e.message}", e)
                        "Recovery failed: ${e.message ?: "Unknown error"}"
                    }
                }
                _passwordRecoveryState.value = PasswordRecoveryState.Error(errorMessage)
            }
        }
    }

    private fun parseRecoveryError(
        errorCode: Int,
        errorMessage: String,
        errorBody: String
    ): String {
        return when (errorCode) {
            400 -> {
                when {
                    errorBody.contains("email", ignoreCase = true) ->
                        "Invalid email address"
                    errorBody.contains("rate limit", ignoreCase = true) ->
                        "Too many requests. Please try again later."
                    else -> "Bad request: $errorMessage"
                }
            }

            401 -> "Unauthorized. Please check your API key."
            403 -> "Forbidden. You don't have permission to perform this action."
            404 -> "User with this email not found."
            422 -> "Invalid email format."
            429 -> "Too many attempts. Please try again in a few minutes."
            500 -> "Server error. Please try again later."
            502 -> "Bad gateway. Service temporarily unavailable."
            503 -> "Service unavailable. Please try again later."
            else -> "Recovery failed: $errorMessage"
        }
    }

    fun resetState() {
        _passwordRecoveryState.value = PasswordRecoveryState.Idle
    }
}

sealed class PasswordRecoveryState {
    object Idle : PasswordRecoveryState()
    object Loading : PasswordRecoveryState()
    data class Success(val message: String) : PasswordRecoveryState()
    data class Error(val message: String) : PasswordRecoveryState()
}
