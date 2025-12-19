package com.example.shoesshop.data.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesshop.R
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.Categories
import com.example.shoesshop.data.models.ProductDto
import com.example.shoesshop.data.models.Products
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {


    private val categoriesApi = RetrofitInstance.databaseService
    private val productsApi = RetrofitInstance.productsService

    private val _categories = mutableStateOf<List<Categories>>(emptyList())
    val categories: State<List<Categories>> = _categories

    private val _productsDto = mutableStateOf<List<ProductDto>>(emptyList())
    val productsDto: State<List<ProductDto>> = _productsDto

    private val _uiProducts = mutableStateOf<List<Products>>(emptyList())
    val uiProducts: State<List<Products>> = _uiProducts

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
            runCatching { productsApi.getThreeProducts() }
                .onSuccess { list ->
                    _productsDto.value = list
                    _uiProducts.value = list.map { dto ->
                        Products(
                            id = dto.id,
                            name = dto.title,
                            price = "₽${dto.cost}",
                            originalPrice = "",
                            category = if (dto.isBestSeller) "BEST SELLER" else "",
                            imageUrl = "",
                            imageResId = getImageResIdForProduct(dto.id))
                    }
                }
                .onFailure { e ->
                    Log.e("HomeViewModel", "loadProducts error", e)
                }
        }
    }

    private val productImageMap = mapOf(
        "21c2d7c0-a2ea-49a2-9198-52bafafc6958" to R.drawable._21c2d7c0_a2ea_49a2_9198_52bafafc6958_999999,
        "3fb5c391-a841-45fb-9c7a-2f74d11a4bfa" to R.drawable._3fb5c391_a841_45fb_9c7a_2f74d11a4bfa_99958c,
        "4f311959-5ce2-4b3c-94b9-e41dd790e571" to R.drawable._4f311959_5ce2_4b3c_94b9_e41dd790e571_000000,
        "60f2ced6-7b81-465d-bc0f-9c3395b78c56" to R.drawable._60f2ced6_7b81_465d_bc0f_9c3395b78c56_000000,
        "63d56e11-2769-4980-96df-dc11ea70148d" to R.drawable._63d56e11_2769_4980_96df_dc11ea70148d_ffffff,
        "64229908-3713-4147-bedc-68ddaef9c67a" to R.drawable._64229908_3713_4147_bedc_68ddaef9c67a_000000,
        "6478da8e-87e6-4a7a-821c-ac38dd861cec" to R.drawable._6478da8e_87e6_4a7a_821c_ac38dd861cec_753413,
        "6bbdb878-4b52-433c-adb4-a742759939fa" to R.drawable._6bbdb878_4b52_433c_adb4_a742759939fa_000000,
        "74bc6394-6695-4134-992b-1d9972aeb639" to R.drawable._74bc6394_6695_4134_992b_1d9972aeb639_ffa500,
        "815aa749-975e-4285-bc49-6921556fb9ad" to R.drawable._815aa749_975e_4285_bc49_6921556fb9ad_999999,
        "9b56e21d-9e2c-4cce-b48d-497397f0ba47" to R.drawable._9b56e21d_9e2c_4cce_b48d_497397f0ba47_000000,
        "9e9c9924-7000-4e7b-8332-33bd8d0bd9b6" to R.drawable._9e9c9924_7000_4e7b_8332_33bd8d0bd9b6_000000,
        "afa022ec-f15b-425e-b9d9-fef6c7da654e" to R.drawable._afa022ec_f15b_425e_b9d9_fef6c7da654e_000000,
        "b12a26f0-31ff-4c54-b6e8-9701a25030fe" to R.drawable._b12a26f0_31ff_4c54_b6e8_9701a25030fe_ffffff,
    )


    private fun getImageResIdForProduct(id: String): Int {
        return productImageMap[id]
            ?: R.drawable.nike_zoom_winflo_3_831561_001_mens_running_shoes_11550187236tiyyje6l87_prev_ui_3
    }
}
