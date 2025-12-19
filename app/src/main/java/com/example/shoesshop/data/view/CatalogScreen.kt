package com.example.shoesshop.data.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoesshop.R
import com.example.shoesshop.data.models.Categories
import com.example.shoesshop.data.models.Products
import com.example.shoesshop.data.viewModel.CatalogViewModel
import com.example.shoesshop.data.viewModel.CatalogViewModelFactory
import com.example.shoesshop.data.viewModel.CartViewModel
import com.example.shoesshop.data.viewModel.FavouriteViewModel
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
    ),
    favouriteViewModel: FavouriteViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val products by viewModel.products
    val categories by viewModel.categories
    val selectedCategoryId by viewModel.selectedCategoryId
    val selectedCategoryName by viewModel.selectedCategoryName

    val context = LocalContext.current
    val favouriteUiState by favouriteViewModel.uiState.collectAsStateWithLifecycle()
    val favouriteIds = favouriteUiState.products.map { it.id }.toSet()

    // при каждом открытии экрана и при смене категории — обновляем товары
    LaunchedEffect(categoryId) {
        viewModel.onCategorySelected(
            Categories(
                id = categoryId,
                name = categoryName,
                isSelected = true
            )
        )
    }

    // при каждом открытии каталога обновляем избранное
    LaunchedEffect(Unit) {
        favouriteViewModel.loadFavourites(context)
    }

    Scaffold(
        containerColor = colorResource(R.color.Background),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.Background)
                ),
                modifier = Modifier.padding(start = 21.dp),
                title = {
                    Text(
                        text = selectedCategoryName ?: categoryName,
                        modifier = Modifier.width(291.dp),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                color = colorResource(R.color.white),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back",
                            tint = colorResource(R.color.Text),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(colorResource(R.color.Background))
                .fillMaxSize()
        ) {
            CategoryBar(
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                onCategorySelected = { viewModel.onCategorySelected(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (products.isEmpty()) {
                    // индикация загрузки каталога
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(products) { product ->
                            val isFav = favouriteIds.contains(product.id)
                            ProductCard(
                                product = product,
                                isFavorite = isFav,
                                onProductClick = { onProductClick(product) },
                                onFavoriteClick = {
                                    favouriteViewModel.toggleFavourite(
                                        context = context,
                                        product = product,
                                        currentlyFavourite = isFav
                                    )
                                },
                                onAddClick = {
                                    cartViewModel.addToCart(context, product)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryBar(
    categories: List<Categories>,
    selectedCategoryId: String?,
    onCategorySelected: (Categories) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.category),
            style = Typography.labelMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    category = category.name,
                    isSelected = selectedCategoryId == category.id,
                    onClick = { onCategorySelected(category) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun CatalogScreenPreview() {
    CatalogScreen("fdsd", "{}", {}, {})
}