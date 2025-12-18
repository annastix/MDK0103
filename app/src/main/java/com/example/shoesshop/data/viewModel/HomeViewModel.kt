package com.example.shoesshop.data.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.Categories
import com.example.shoesshop.data.models.ProductDto
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val categoriesApi = RetrofitInstance.databaseService
    private val productsApi = RetrofitInstance.productsService

    private val _categories = mutableStateOf<List<Categories>>(emptyList())
    val categories: State<List<Categories>> = _categories

    private val _products = mutableStateOf<List<ProductDto>>(emptyList())
    val products: State<List<ProductDto>> = _products

    init {
        loadCategories()
        loadProducts()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            runCatching { categoriesApi.getCategories() }
                .onSuccess { list ->
                    val all = Categories(id = "all", name = "All", isSelected = true)
                    _categories.value = listOf(all) + list
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadCategories error", e)
                }
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            runCatching { productsApi.getProducts() }
                .onSuccess { list ->
                    _products.value = list
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadProducts error", e)
                }
        }
    }
}
