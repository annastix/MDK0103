package com.example.shoesshop.ui.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shoesshop.data.ProductImages
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.Categories
import com.example.shoesshop.data.models.ProductDto
import com.example.shoesshop.data.models.Products
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val initialCategoryId: String
) : ViewModel() {

    private val categoriesApi = RetrofitInstance.databaseService
    private val productsApi = RetrofitInstance.productsService

    private val _categories = mutableStateOf<List<Categories>>(emptyList())
    val categories: State<List<Categories>> = _categories

    private val _selectedCategoryId = mutableStateOf<String?>(initialCategoryId)
    val selectedCategoryId: State<String?> = _selectedCategoryId

    private val _selectedCategoryName = mutableStateOf<String?>(null)
    val selectedCategoryName: State<String?> = _selectedCategoryName

    private val _productsDto = mutableStateOf<List<ProductDto>>(emptyList())
    private val _products = mutableStateOf<List<Products>>(emptyList())
    val products: State<List<Products>> = _products

    init {
        _selectedCategoryId.value = initialCategoryId
        loadCategories()
        loadProductsFor(initialCategoryId)
    }

    private fun loadCategories() {
        viewModelScope.launch {
            runCatching { categoriesApi.getCategories() }
                .onSuccess { list ->
                    val all = Categories(id = "all", name = "All", isSelected = false)
                    val result = listOf(all) + list
                    _categories.value = result

                    if (_selectedCategoryName.value == null) {
                        _selectedCategoryName.value =
                            result.firstOrNull { it.id == initialCategoryId }?.name ?: "All"
                    }
                }
                .onFailure { e -> Log.e("CatalogViewModel", "loadCategories error", e) }
        }
    }

    private fun loadProductsFor(categoryId: String?) {
        viewModelScope.launch {
            runCatching {
                if (categoryId == null || categoryId == "all") {
                    productsApi.getProducts()
                } else {
                    productsApi.getProductsByCategory("eq.$categoryId")
                }
            }.onSuccess { list ->
                _productsDto.value = list
                _products.value = list.map { dto ->
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
            }.onFailure { e ->
                Log.e("CatalogViewModel", "loadProductsFor error", e)
            }
        }
    }


    fun onCategorySelected(category: Categories) {
        _selectedCategoryId.value = category.id
        _selectedCategoryName.value = category.name
        loadProductsFor(category.id)
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
