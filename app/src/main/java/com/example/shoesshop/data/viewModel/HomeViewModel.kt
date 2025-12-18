package com.example.shoesshop.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.Categories
import com.example.shoesshop.data.service.DatabaseService
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.shoesshop.data.RetrofitInstance.databaseService

class HomeViewModel : ViewModel() {



    private val _categories = mutableStateOf<List<Categories>>(emptyList())
    val categories: State<List<Categories>> = _categories

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            runCatching { databaseService.getCategories() }
                .onSuccess { list ->
                    // Добавляем "All" вручную и помечаем его выбранным
                    val all = Categories(
                        id = "all",
                        name = "All",
                        isSelected = true
                    )
                    _categories.value = listOf(all) + list
                }
                .onFailure {
                    // обработка ошибки (лог/стейт)
                }
        }
    }
}
