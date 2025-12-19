package com.example.shoesshop.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.shoesshop.data.PreferenceHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ResetOtpUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ResetPasswordOTPViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ResetOtpUiState())
    val uiState: StateFlow<ResetOtpUiState> = _uiState

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun verifyOtp(
        token: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (token.length != 6) {
            val msg = "Код должен содержать 6 цифр"
            _uiState.value = _uiState.value.copy(errorMessage = msg)
            onError(msg)
            return
        }

        // просто сохраняем OTP и идём на экран нового пароля
        PreferenceHelper.saveResetOtp(context, token)
        onSuccess()
    }
}
