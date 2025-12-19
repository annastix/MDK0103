package com.example.shoesshop.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.OrderItemRequest
import com.example.shoesshop.data.models.OrderRequest

class CheckoutViewModel(
    private val cartViewModel: CartViewModel
) : ViewModel() {

    private val ordersApi = RetrofitInstance.ordersService

    var isSaving by mutableStateOf(false)
        private set

    suspend fun saveOrder(
        email: String?,
        phone: String?,
        address: String?,
        deliveryCoast: Long,
        userId: String,
        paymentId: String?,
        statusId: String = "970aed1e-549c-499b-a649-4bf3f9f93a01"
    ) {
        isSaving = true
        try {
            val cartItems = cartViewModel.currentCartItems()
            if (cartItems.isEmpty()) {
                Log.e("CheckoutViewModel", "saveOrder: cart is empty")
                return
            }

            // 1. Создаём заказ
            try {
                ordersApi.createOrder(
                    OrderRequest(
                        email = email,
                        phone = phone,
                        address = address,
                        user_id = userId,
                        payment_id = paymentId,
                        delivery_coast = deliveryCoast,
                        status_id = statusId
                    )
                )
                Log.d("CheckoutViewModel", "createOrder OK")
            } catch (e: Exception) {
                Log.e("CheckoutViewModel", "createOrder FAILED", e)
                return
            }

            // 2. Берём id последнего заказа пользователя
            val orders = try {
                ordersApi.getLastOrder(userIdFilter = "eq.$userId")
            } catch (e: Exception) {
                Log.e("CheckoutViewModel", "getLastOrder FAILED", e)
                return
            }

            if (orders.isEmpty()) {
                Log.e("CheckoutViewModel", "getLastOrder: empty list")
                return
            }

            val orderId = orders.first().id
            Log.d("CheckoutViewModel", "Last order id = $orderId")

            // 3. Готовим позиции для orders_items
            val items = cartItems.map { item ->
                OrderItemRequest(
                    title = item.title,
                    coast = item.cost,
                    count = item.count.toLong(),
                    order_id = orderId,
                    product_id = item.productId
                )
            }

            // 4. Сохраняем позиции
            try {
                ordersApi.addItems(items)
                Log.d("CheckoutViewModel", "addItems OK, count=${items.size}")
            } catch (e: Exception) {
                Log.e("CheckoutViewModel", "addItems FAILED", e)
                return
            }

            // 5. Очищаем корзину на сервере
            try {
                cartViewModel.clearCartOnServer(userId)
                Log.d("CheckoutViewModel", "Order finished, cart cleared")
            } catch (e: Exception) {
                Log.e("CheckoutViewModel", "clearCartOnServer FAILED", e)
            }
        } finally {
            isSaving = false
        }
    }
}