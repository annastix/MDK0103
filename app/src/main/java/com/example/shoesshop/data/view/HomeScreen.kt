package com.example.shoesshop.data.view

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoesshop.R
import com.example.shoesshop.data.models.Categories
import com.example.shoesshop.data.models.Products
import com.example.shoesshop.data.viewModel.CartViewModel
import com.example.shoesshop.data.viewModel.FavouriteViewModel
import com.example.shoesshop.data.viewModel.HomeViewModel
import com.example.shoesshop.ui.components.ProductCard
import com.example.shoesshop.ui.theme.AppTypography
import com.example.shoesshop.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProductClick: (Products) -> Unit = {},
    onCartClick: () -> Unit = {},                 // переход в корзину
    onSearchClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onCategoryClick: (String, String) -> Unit = { _, _ -> },
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(
            LocalContext.current.applicationContext as Application
        )
    ),
    favouriteViewModel: FavouriteViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    var selected by rememberSaveable { mutableIntStateOf(0) }

    val categoriesState by viewModel.categories
    val uiProductsState by viewModel.uiProducts
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    // состояние избранного
    val favouriteUiState by favouriteViewModel.uiState.collectAsStateWithLifecycle()
    val favouriteIds = favouriteUiState.products.map { it.id }.toSet()

    // состояние корзины (список productId)
    val cartProductIds by cartViewModel.productIdsInCart.collectAsStateWithLifecycle()

    // по умолчанию выбираем первую категорию
    LaunchedEffect(categoriesState) {
        if (selectedCategoryId == null && categoriesState.isNotEmpty()) {
            selectedCategoryId = categoriesState.first().id
        }
    }

    // загружаем избранное и корзину каждый раз, когда выбран таб Home
    LaunchedEffect(selected) {
        if (selected == 0) {
            favouriteViewModel.loadFavourites(context)
            cartViewModel.loadCart(context)
        }
    }

    Scaffold(
        bottomBar = {
            BottomBar(
                selected = selected,
                onSelectedChange = { selected = it },
                onCartClick = onCartClick          // FAB → корзина
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(color = colorResource(R.color.Background))
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (selected == 0) {
                TopBar(onSettingsClick = onSettingsClick)
            }

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when (selected) {
                    0 -> {
                        if (categoriesState.isEmpty() && uiProductsState.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                item {
                                    CategorySection(
                                        categories = categoriesState,
                                        selectedCategoryId = selectedCategoryId,
                                        onCategorySelected = { cat ->
                                            selectedCategoryId = cat.id
                                            onCategoryClick(cat.id, cat.name)
                                        }
                                    )
                                }

                                item {
                                    ProductsSection(
                                        products = uiProductsState,
                                        onProductClick = onProductClick,
                                        onFavoriteClick = { product ->
                                            val currentlyFavourite =
                                                favouriteIds.contains(product.id)
                                            favouriteViewModel.toggleFavourite(
                                                context = context,
                                                product = product,
                                                currentlyFavourite = currentlyFavourite
                                            )
                                        },
                                        favouriteIds = favouriteIds,
                                        cartProductIds = cartProductIds,
                                        onAddClick = { product ->
                                            cartViewModel.addToCart(context, product)
                                        },
                                        onOpenCart = onCartClick
                                    )
                                }

                                item {
                                    PromotionsSection()
                                }
                            }
                        }
                    }
                    1 -> FavoriteScreen(
                        onBackClick = { selected = 0 },
                        onProductClick = onProductClick,
                        onCartClick = onCartClick
                    )
                    2 -> CenterText("Уведомления")
                    3 -> ProfileScreen()
                }
            }
        }
    }
}

@Composable
private fun TopBar(onSettingsClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.main),
            style = Typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    placeholder = {
                        Text(
                            text = "Поиск...",
                            style = AppTypography.bodyMedium14
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Поиск",
                            tint = Color.Gray
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(colorResource(R.color.Accent))
                    .clickable { onSettingsClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.sliders),
                    contentDescription = "Настройки",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun BottomBar(
    selected: Int,
    onSelectedChange: (Int) -> Unit,
    onCartClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.panel),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // слева: Home + Favorites
            Row {
                IconButton(onClick = { onSelectedChange(0) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.home),
                        contentDescription = "Home",
                        tint = if (selected == 0)
                            colorResource(R.color.Accent)
                        else
                            colorResource(R.color.black)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = { onSelectedChange(1) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.favorite),
                        contentDescription = "Favorites",
                        tint = if (selected == 1)
                            colorResource(R.color.Accent)
                        else
                            colorResource(R.color.black)
                    )
                }
            }

            // центр: корзина (FAB)
            Box(
                modifier = Modifier
                    .offset(y = (-20).dp)
                    .size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton(
                    onClick = { onCartClick() },          // ТОЛЬКО корзина
                    modifier = Modifier.size(56.dp),
                    containerColor = colorResource(R.color.Accent),
                    contentColor = colorResource(R.color.white),
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.bag_2),
                        contentDescription = "Cart",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // справа: уведомления + профиль
            Row {
                IconButton(onClick = { onSelectedChange(2) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.notification),
                        contentDescription = "Notification",
                        tint = if (selected == 2)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = { onSelectedChange(3) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile",
                        tint = if (selected == 3)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun CategorySection(
    categories: List<Categories>,
    selectedCategoryId: String?,
    onCategorySelected: (Categories) -> Unit
) {
    Column {
        Text(
            text = stringResource(id = R.string.category),
            style = Typography.labelMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
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

@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(108.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = if (isSelected) colorResource(R.color.Accent) else colorResource(R.color.white),
        contentColor = if (isSelected) Color.White else Color.Black
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category,
                style = Typography.displaySmall
            )
        }
    }
}

@Composable
private fun ProductsSection(
    products: List<Products>,
    onProductClick: (Products) -> Unit,
    onFavoriteClick: (Products) -> Unit,
    favouriteIds: Set<String>,
    cartProductIds: Set<String>,
    onAddClick: (Products) -> Unit,
    onOpenCart: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.popular),
                style = Typography.labelMedium
            )
            Text(
                text = stringResource(R.string.all),
                style = Typography.displaySmall,
                color = colorResource(R.color.Accent),
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                val isFav = favouriteIds.contains(product.id)
                val inCart = cartProductIds.contains(product.id)

                Box(
                    modifier = Modifier
                        .width(160.dp)   // фиксированная ширина
                        .height(270.dp)  // фиксированная высота (под дизайн)
                ) {
                    ProductCard(
                        product = product,
                        isFavorite = isFav,
                        onProductClick = { onProductClick(product) },
                        onFavoriteClick = { onFavoriteClick(product) },
                        onAddClick = {
                            if (inCart) onOpenCart() else onAddClick(product)
                        },
                        inCart = inCart
                    )
                }
            }
        }
    }
}

@Composable
private fun PromotionsSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = stringResource(id = R.string.arrivals),
                style = Typography.labelMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                text = stringResource(R.string.all),
                style = Typography.displaySmall,
                color = colorResource(R.color.Accent),
                modifier = Modifier.clickable { }
            )
        }

        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(R.drawable.summer_sale),
            contentScale = ContentScale.Crop,
            contentDescription = "sale"
        )
    }
}

@Composable
private fun CenterText(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = Typography.headlineMedium)
    }
}

class HomeViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
