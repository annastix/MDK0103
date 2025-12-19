package com.example.shoesshop.data.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.OrderHistoryDto
import com.example.shoesshop.data.service.API_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class OrdersUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val orders: List<OrderHistoryDto> = emptyList()
)

class OrdersViewModel : ViewModel() {

    private val ordersApi = RetrofitInstance.ordersService

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState

    fun loadOrders(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            runCatching {
                ordersApi.getOrders(
                    auth = "Bearer $API_KEY",
                    apiKey = API_KEY,
                    userIdFilter = "eq.$userId"
                )
            }.onSuccess { list ->
                _uiState.value = OrdersUiState(
                    isLoading = false,
                    errorMessage = null,
                    orders = list
                )
            }.onFailure { e ->
                Log.e("OrdersViewModel", "loadOrders error", e)
                _uiState.value = OrdersUiState(
                    isLoading = false,
                    errorMessage = e.message ?: "Ошибка загрузки заказов",
                    orders = emptyList()
                )
            }
        }
    }
}
