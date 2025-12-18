// CatalogScreen.kt
package com.example.shoesshop.data.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoesshop.R
import com.example.shoesshop.data.models.Products
import com.example.shoesshop.data.viewModel.CatalogViewModel
import com.example.shoesshop.data.viewModel.CatalogViewModelFactory
import com.example.shoesshop.ui.components.ProductCard
import com.example.shoesshop.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    categoryId: String,
    categoryName: String,
    onBackClick: () -> Unit,
    onProductClick: (Products) -> Unit,
    viewModel: CatalogViewModel = viewModel(
        factory = CatalogViewModelFactory(categoryId)
    )
) {
    val products by viewModel.products

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .background(colorResource(R.color.Background))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Категории",
                    style = Typography.labelMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onProductClick(product) },
                            onFavoriteClick = { /* TODO: избранное */ }
                        )
                    }
                }
            }
        }
    }
}
