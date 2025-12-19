package com.example.shoesshop.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.shoesshop.data.ProductImages
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.Products
import com.example.shoesshop.data.service.API_KEY
import com.example.shoesshop.data.service.CartInsertRequest
import com.example.shoesshop.data.service.CartUpdateRequest
import com.example.shoesshop.ui.view.CartItemUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CartScreenUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val items: List<CartItemUi> = emptyList()
)

/** Элементы корзины в формате, удобном для orders_items. */
data class CartItemForOrder(
    val productId: String,
    val title: String,
    val cost: Double,
    val count: Int
)

class CartViewModel : ViewModel() {

    private val cartApi = RetrofitInstance.cartService

    private val _uiState = MutableStateFlow(CartScreenUiState())
    val uiState: StateFlow<CartScreenUiState> = _uiState

    // список productId, которые уже в корзине
    private val _productIdsInCart = MutableStateFlow<Set<String>>(emptySet())
    val productIdsInCart: StateFlow<Set<String>> = _productIdsInCart

    fun loadCart(context: Context) {
        val userId = getSavedUserId(context)
        if (userId == null) {
            _uiState.value = CartScreenUiState(
                isLoading = false,
                errorMessage = "Пользователь не авторизован"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching {
                cartApi.getCart(
                    auth = "Bearer $API_KEY",
                    apiKey = API_KEY,
                    userIdFilter = "eq.$userId"
                )
            }.onSuccess { list ->
                val mapped = list.map { dto ->
                    CartItemUi(
                        cartId = dto.id,
                        productId = dto.productId ?: "",
                        title = dto.products?.title.orEmpty(),
                        price = dto.products?.cost ?: 0.0,
                        imageResId = ProductImages.forId(dto.productId ?: ""),
                        count = dto.count ?: 1
                    )
                }
                _uiState.value = CartScreenUiState(
                    isLoading = false,
                    errorMessage = null,
                    items = mapped
                )
                _productIdsInCart.value = list.mapNotNull { it.productId }.toSet()
            }.onFailure { e ->
                Log.e("CartViewModel", "loadCart error", e)
                _uiState.value = CartScreenUiState(
                    isLoading = false,
                    errorMessage = e.message ?: "Ошибка загрузки корзины",
                    items = emptyList()
                )
            }
        }
    }

    // ------- изменение количества -------

    fun increase(cartId: String) {
        val current = _uiState.value.items.find { it.cartId == cartId } ?: return
        val newCount = current.count + 1
        updateCountRemote(cartId, newCount)
        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items.map {
                if (it.cartId == cartId) it.copy(count = newCount) else it
            }
        )
    }

    fun decrease(cartId: String) {
        val current = _uiState.value.items.find { it.cartId == cartId } ?: return
        if (current.count <= 1) return
        val newCount = current.count - 1
        updateCountRemote(cartId, newCount)
        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items.map {
                if (it.cartId == cartId) it.copy(count = newCount) else it
            }
        )
    }

    private fun updateCountRemote(cartId: String, newCount: Int) {
        viewModelScope.launch {
            runCatching {
                cartApi.updateCartItem(
                    auth = "Bearer $API_KEY",
                    apiKey = API_KEY,
                    idFilter = "eq.$cartId",
                    body = CartUpdateRequest(count = newCount)
                )
            }.onFailure { e ->
                Log.e("CartViewModel", "updateCountRemote error", e)
            }
        }
    }

    // ------- удаление -------

    fun remove(cartId: String) {
        val removed = _uiState.value.items.find { it.cartId == cartId }
        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items.filterNot { it.cartId == cartId }
        )
        if (removed != null) {
            _productIdsInCart.value = _productIdsInCart.value - removed.productId
        }

        viewModelScope.launch {
            runCatching {
                cartApi.deleteCartItem(
                    auth = "Bearer $API_KEY",
                    apiKey = API_KEY,
                    idFilter = "eq.$cartId"
                )
            }.onFailure { e ->
                Log.e("CartViewModel", "deleteCartItem error", e)
            }
        }
    }

    // ------- добавление товара -------

    fun addToCart(context: Context, product: Products) {
        val userId = getSavedUserId(context) ?: return
        if (_productIdsInCart.value.contains(product.id)) return

        viewModelScope.launch {
            runCatching {
                cartApi.addCartItem(
                    auth = "Bearer $API_KEY",
                    apiKey = API_KEY,
                    body = CartInsertRequest(
                        userId = userId,
                        productId = product.id,
                        count = 1
                    )
                )
            }.onSuccess {
                _productIdsInCart.value = _productIdsInCart.value + product.id
            }.onFailure { e ->
                Log.e("CartViewModel", "addToCart error", e)
            }
        }
    }

    // ------- user_id -------

    fun getSavedUserId(context: Context): String? {
        return try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val securePrefs = EncryptedSharedPreferences.create(
                context,
                "secure_user_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            securePrefs.getString("user_id", null)
                ?: context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    .getString("user_id", null)
        } catch (e: Exception) {
            context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("user_id", null)
        }
    }

    // ------- данные для Checkout -------

    fun currentCartItems(): List<CartItemForOrder> {
        return _uiState.value.items.map {
            CartItemForOrder(
                productId = it.productId,
                title = it.title,
                cost = it.price,
                count = it.count
            )
        }
    }


    suspend fun clearCartOnServer(userId: String) {
        runCatching {
            cartApi.deleteCartByUser(
                auth = "Bearer $API_KEY",
                apiKey = API_KEY,
                userIdFilter = "eq.$userId"
            )
        }.onFailure { e ->
            Log.e("CartViewModel", "clearCartOnServer error", e)
            return
        }

        _uiState.value = _uiState.value.copy(items = emptyList())
        _productIdsInCart.value = emptySet()
    }

}
