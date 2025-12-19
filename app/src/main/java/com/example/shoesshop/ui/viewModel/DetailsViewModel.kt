package com.example.shoesshop.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shoesshop.data.ProductImages
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.ProductDto
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val productId: String
) : ViewModel() {

    private val api = RetrofitInstance.productsService

    var state by mutableStateOf<DetailsUiState>(DetailsUiState.Loading)
        private set

    init {
        loadProduct()
    }

    fun loadProduct() {
        viewModelScope.launch {
            state = DetailsUiState.Loading
            runCatching {
                api.getProductById("eq.$productId").firstOrNull()
            }.onSuccess { dto ->
                if (dto == null) {
                    state = DetailsUiState.Error("Товар не найден")
                } else {
                    val imageResId = ProductImages.forId(dto.id)
                    state = DetailsUiState.Success(dto, imageResId)
                }
            }.onFailure { e ->
                state = DetailsUiState.Error(
                    "Ошибка при загрузке товара: ${e.localizedMessage ?: "неизвестная ошибка"}"
                )
            }
        }
    }
}

sealed class DetailsUiState {
    object Loading : DetailsUiState()
    data class Success(
        val product: ProductDto,
        val imageResId: Int
    ) : DetailsUiState()
    data class Error(val message: String) : DetailsUiState()
}

class DetailsViewModelFactory(
    private val productId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailsViewModel(productId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
