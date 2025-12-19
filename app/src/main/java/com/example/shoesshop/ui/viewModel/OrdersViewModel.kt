package com.example.shoesshop.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.OrderHistoryDto
import com.example.shoesshop.data.models.OrderItemRequest
import com.example.shoesshop.data.models.OrderRequest
import com.example.shoesshop.data.service.API_KEY
import com.example.shoesshop.data.service.CancelOrderRequest
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
                ordersApi.fetchOrdersHistory(
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

    /** Повтор заказа: создаём новый заказ и такие же позиции. */
    fun repeatOrder(order: OrderHistoryDto, userId: String) {
        viewModelScope.launch {
            runCatching {
                val items = order.orders_items ?: emptyList()

                // 1. создаём заказ
                val newOrder = OrderRequest(
                    email = null,
                    phone = null,
                    address = null,
                    user_id = userId,
                    payment_id = null,
                    delivery_coast = order.delivery_coast ?: 0,
                    status_id = "new"
                )

                val created = ordersApi.createNewOrder(
                    auth = "Bearer $API_KEY",
                    apiKey = API_KEY,
                    body = newOrder
                )

                // 2. добавляем позиции к новому заказу
                val newItems: List<OrderItemRequest> = items.map {
                    OrderItemRequest(
                        title = it.title,
                        coast = it.coast,
                        count = it.count,
                        order_id = created.id,
                        product_id = it.product_id
                    )
                }

                ordersApi.addOrderItems(
                    auth = "Bearer $API_KEY",
                    apiKey = API_KEY,
                    items = newItems
                )

                // 3. обновляем историю
                loadOrders(userId)
            }.onFailure { e ->
                Log.e("OrdersViewModel", "repeatOrder error", e)
            }
        }
    }

    /** Отмена заказа: PATCH status_id = "canceled". */
    fun cancelOrder(orderId: Long, userId: String) {
        viewModelScope.launch {
            runCatching {
                ordersApi.updateOrderStatus(
                    auth = "Bearer $API_KEY",
                    apiKey = API_KEY,
                    idFilter = "eq.$orderId",
                    body = CancelOrderRequest()
                )
            }.onSuccess {
                // локально помечаем как отменённый
                val updated = _uiState.value.orders.map {
                    if (it.id == orderId) it.copy(status_id = "canceled") else it
                }
                _uiState.value = _uiState.value.copy(orders = updated)
            }.onFailure { e ->
                Log.e("OrdersViewModel", "cancelOrder error", e)
            }
        }
    }
}