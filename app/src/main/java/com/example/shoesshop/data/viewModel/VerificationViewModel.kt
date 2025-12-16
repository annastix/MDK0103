package com.example.shoesshop.data.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.VerifyOTPRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class VerificationUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isVerified: Boolean = false
)

class VerificationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(VerificationUiState())
    val uiState: StateFlow<VerificationUiState> = _uiState

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun getEmailFromSharedPreferences(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_email", "") ?: ""
    }

    private fun saveAuthToken(context: Context, token: String?) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.apply()
        Log.d("VerificationViewModel", "Auth token saved")
    }

    fun getStoredEmail(context: Context): String {
        return getEmailFromSharedPreferences(context)
    }

    private fun markUserAsVerified(context: Context) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_user_verified", true)
        editor.apply()
        Log.d("VerificationViewModel", "User marked as verified")
    }

    fun verifyOTP(
        token: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // Простая валидация на стороне ViewModel
        if (token.isEmpty()) {
            onError("Please enter OTP token")
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter OTP token")
            return
        }

        val userEmail = getEmailFromSharedPreferences(context)
        if (userEmail.isEmpty()) {
            onError("User email not found. Please restart registration.")
            _uiState.value = _uiState.value.copy(
                errorMessage = "User email not found. Please restart registration."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val verifyOtpRequest = VerifyOTPRequest(
                    email = userEmail,
                    token = token,
                    type = "signup"
                )

                val response = RetrofitInstance.userManagementService.verifyOtp(verifyOtpRequest)

                if (response.isSuccessful) {
                    response.body()?.let { verifyResponse ->
                        Log.v("VerificationViewModel", "OTP verified successfully")
                        Log.d("VerificationViewModel", "Access token: ${verifyResponse.accessToken}")

                        saveAuthToken(context, verifyResponse.accessToken)
                        markUserAsVerified(context)

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isVerified = true
                        )

                        onSuccess()
                    } ?: run {
                        val errorMsg = "Empty response from server"
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = errorMsg
                        )
                        onError(errorMsg)
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        400 -> "Invalid request. Please check your OTP code"
                        401 -> "Invalid OTP token"
                        404 -> "User not found. Please register again"
                        422 -> "Invalid OTP or expired"
                        else -> "OTP verification failed: ${response.message()}"
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )

                    onError(errorMessage)
                }
            } catch (e: Exception) {
                Log.e("VerificationViewModel", "OTP verification error", e)

                val errorMessage = "Network error: ${e.localizedMessage ?: e.message}"
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = errorMessage
                )

                onError(errorMessage)
            }
        }
    }
}