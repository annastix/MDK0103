package com.example.shoesshop.data.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shoesshop.R
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.ProductDto
import com.example.shoesshop.data.models.Products
import kotlinx.coroutines.launch

// CatalogViewModel.kt
class CatalogViewModel(private val categoryId: String) : ViewModel() {

    private val api = RetrofitInstance.productsService

    private val _products = mutableStateOf<List<Products>>(emptyList())
    val products: State<List<Products>> = _products

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            runCatching {
                api.getProductsByCategory("eq.$categoryId")
            }.onSuccess { list ->
                _products.value = list.map { dto ->
                    Products(
                        id = dto.id,
                        name = dto.title,
                        price = "₽${dto.cost}",
                        originalPrice = "",
                        category = if (dto.isBestSeller) "BEST SELLER" else "",
                        imageUrl = "",
                        imageResId = R.drawable.nike_zoom_winflo_3_831561_001_mens_running_shoes_11550187236tiyyje6l87_prev_ui_3
                    )
                }
            }.onFailure { e ->
                Log.e("CatalogViewModel", "loadProducts error", e)
            }
        }
    }
}


class CatalogViewModelFactory(
    private val categoryId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatalogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatalogViewModel(categoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
