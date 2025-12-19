// data/viewModel/FavouriteViewModel.kt
package com.example.shoesshop.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesshop.data.PreferenceHelper
import com.example.shoesshop.data.ProductImages
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.Products
import com.example.shoesshop.data.service.FavouriteRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FavouriteUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val products: List<Products> = emptyList()
)

class FavouriteViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FavouriteUiState())
    val uiState: StateFlow<FavouriteUiState> = _uiState

    fun loadFavourites(context: Context) {
        val userId = PreferenceHelper.getUserId(context)
        if (userId.isNullOrEmpty()) {
            _uiState.value = FavouriteUiState(error = "Не найден user_id")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val resp = RetrofitInstance.favouriteService
                    .getFavouriteProducts("eq.$userId")

                Log.d(
                    "FavouriteVM",
                    "code=${resp.code()} body=${resp.body()} err=${resp.errorBody()?.string()}"
                )

                if (resp.isSuccessful) {
                    val list = resp.body().orEmpty().map { wrapper ->
                        val dto = wrapper.products
                        Products(
                            id = dto.id,
                            name = dto.title,
                            price = "₽${dto.cost}",
                            originalPrice = "",
                            category = if (dto.isBestSeller) "BEST SELLER" else "",
                            imageUrl = "",
                            imageResId = ProductImages.forId(dto.id)
                        )
                    }
                    _uiState.value = _uiState.value.copy(
                        products = list,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Ошибка загрузки избранного: ${resp.code()}",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("FavouriteVM", "loadFavourites error", e)
                _uiState.value = _uiState.value.copy(
                    error = "Ошибка сети: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun toggleFavourite(
        context: Context,
        product: Products,
        currentlyFavourite: Boolean
    ) {
        val userId = PreferenceHelper.getUserId(context) ?: return

        viewModelScope.launch {
            try {
                if (currentlyFavourite) {
                    RetrofitInstance.favouriteService.removeFromFavourite(
                        productIdEq = "eq.${product.id}",
                        userIdEq = "eq.$userId"
                    )
                } else {
                    RetrofitInstance.favouriteService.addToFavourite(
                        FavouriteRequest(
                            product_id = product.id,
                            user_id = userId
                        )
                    )
                }
                loadFavourites(context)
            } catch (e: Exception) {
                Log.e("FavouriteVM", "toggleFavourite error", e)
            }
        }
    }
}
