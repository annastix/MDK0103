package com.example.shoesshop.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesshop.data.PreferenceHelper
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.service.Profile
import com.example.shoesshop.data.service.CreateProfileRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val profile: Profile? = null
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadProfile(context: Context) {
        val userId = PreferenceHelper.getUserId(context)
        if (userId.isNullOrEmpty()) {
            _uiState.value = ProfileUiState(error = "Не найден user_id")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val resp = RetrofitInstance.profileService.getProfile("eq.$userId")
                if (resp.isSuccessful) {
                    val profile = resp.body().orEmpty().firstOrNull()
                    _uiState.value = _uiState.value.copy(
                        profile = profile,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Ошибка загрузки профиля: ${resp.code()}",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("ProfileVM", "loadProfile error", e)
                _uiState.value = _uiState.value.copy(
                    error = "Ошибка сети: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun saveProfile(
        context: Context,
        firstname: String,
        lastname: String,
        address: String,
        phone: String
    ) {
        val userId = PreferenceHelper.getUserId(context)
        Log.d("ProfileVM", "saveProfile called, userId=$userId")
        if (userId.isNullOrEmpty()) {
            _uiState.value = _uiState.value.copy(error = "Не найден user_id")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val body = CreateProfileRequest(
                user_id = userId,
                firstname = firstname,
                lastname = lastname,
                address = address,
                phone = phone
            )
            try {
                val current = _uiState.value.profile
                val resp = if (current?.id != null) {
                    // обновляем существующий профиль
                    RetrofitInstance.profileService.updateProfile(
                        profileIdEq = "eq.${current.id}",
                        body = body
                    )
                } else {
                    // создаём новый профиль
                    RetrofitInstance.profileService.createProfile(body)
                }
                Log.d("ProfileVM", "saveProfile code=${resp.code()} body=${resp.body()} err=${resp.errorBody()?.string()}")
                if (resp.isSuccessful) {
                    val profile = resp.body().orEmpty().firstOrNull()
                    _uiState.value = _uiState.value.copy(
                        profile = profile,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Ошибка сохранения профиля: ${resp.code()}",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("ProfileVM", "saveProfile error", e)
                _uiState.value = _uiState.value.copy(
                    error = "Ошибка сети: ${e.message}",
                    isLoading = false
                )
            }
        }
    }
}
