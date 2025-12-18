package com.example.shoesshop.data.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.VerifyOTPRequest
import com.example.shoesshop.data.service.CreateProfileRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class VerificationUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isVerified: Boolean = false
)

class VerificationViewModel : ViewModel() {

    private val profileService = RetrofitInstance.profileService

    private val _uiState = MutableStateFlow(VerificationUiState())
    val uiState: StateFlow<VerificationUiState> = _uiState

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun getEmailFromSharedPreferences(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_email", "") ?: ""
    }

    private fun getNameFromSharedPreferences(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_name", null)
    }

    private fun saveAuthToken(context: Context, token: String?) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString("auth_token", token)
            .apply()
        Log.d("VerificationViewModel", "Auth token saved: $token")
    }

    fun getStoredEmail(context: Context): String {
        return getEmailFromSharedPreferences(context)
    }

    private fun markUserAsVerified(context: Context) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putBoolean("is_user_verified", true)
            .apply()
        Log.d("VerificationViewModel", "User marked as verified")
    }

    fun verifyOTP(
        token: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (token.isEmpty()) {
            val msg = "Please enter OTP token"
            onError(msg)
            _uiState.value = _uiState.value.copy(errorMessage = msg)
            return
        }

        val userEmail = getEmailFromSharedPreferences(context)
        if (userEmail.isEmpty()) {
            val msg = "User email not found. Please restart registration."
            onError(msg)
            _uiState.value = _uiState.value.copy(errorMessage = msg)
            return
        }

        val userName = getNameFromSharedPreferences(context)

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val verifyOtpRequest = VerifyOTPRequest(
                    email = userEmail,
                    token = token,
                    type = "signup"
                )

                val response = RetrofitInstance.userManagementService.verifyOtp(verifyOtpRequest)

                Log.d(
                    "VerificationViewModel",
                    "verifyOtp code=${response.code()} message=${response.message()}"
                )

                if (response.isSuccessful) {
                    val verifyResponse = response.body()
                    if (verifyResponse == null) {
                        val errorMsg = "Empty response from server"
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = errorMsg
                        )
                        onError(errorMsg)
                        return@launch
                    }

                    Log.v("VerificationViewModel", "OTP verified successfully")

                    val accessToken =
                        verifyResponse.session?.accessToken ?: verifyResponse.accessToken
                    Log.d("VerificationViewModel", "Access token: $accessToken")

                    saveAuthToken(context, accessToken)
                    markUserAsVerified(context)

                    val userId = verifyResponse.user?.id
                    if (!userId.isNullOrBlank()) {
                        try {
                            val firstName = userName
                            val resp = profileService.createProfile(
                                CreateProfileRequest(
                                    user_id = userId,
                                    firstname = firstName,
                                    lastname = null,
                                    address = null,
                                    phone = null
                                )
                            )

                            Log.d(
                                "VerificationVM",
                                "createProfile code=${resp.code()} body=${resp.errorBody()?.string()}"
                            )
                        } catch (e: Exception) {
                            Log.e("VerificationViewModel", "Create profile error", e)
                        }
                    } else {
                        Log.w("VerificationViewModel", "User id is null in verifyResponse")
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isVerified = true
                    )
                    onSuccess()
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
