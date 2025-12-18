package com.example.shoesshop.data.view

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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoesshop.R
import com.example.shoesshop.data.models.Categories
import com.example.shoesshop.data.models.Products
import com.example.shoesshop.ui.components.ProductCard
import com.example.shoesshop.ui.theme.AppTypography
import com.example.shoesshop.ui.theme.Typography
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoesshop.data.viewModel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProductClick: (Products) -> Unit,
    onCartClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    viewModel: HomeViewModel = viewModel() // ← получаем VM
) {
    var selected by rememberSaveable { mutableIntStateOf(0) }

    // категории из ViewModel
    val categoriesState by viewModel.categories
    var selectedCategory by remember { mutableStateOf("All") }

    // пока товары оставим захардкоженными
    val popularProducts = listOf(
        Products(
            id = "1",
            name = "Nike Air Max",
            price = "P752.00",
            originalPrice = "P850.00",
            category = "BEST SELLER",
            imageUrl = "",
            imageResId = R.drawable.nike_zoom_winflo_3_831561_001_mens_running_shoes_11550187236tiyyje6l87_prev_ui_3
        ),
        Products(
            id = "2",
            name = "Nike Air Force 1",
            price = "P820.00",
            originalPrice = "P900.00",
            category = "BEST SELLER",
            imageUrl = "",
            imageResId = R.drawable.nike_zoom_winflo_3_831561_001_mens_running_shoes_11550187236tiyyje6l87_prev_ui_3
        ),
        Products(
            id = "3",
            name = "Adidas Ultraboost",
            price = "P680.00",
            originalPrice = "P750.00",
            category = "NEW",
            imageUrl = "",
            imageResId = R.drawable.nike_zoom_winflo_3_831561_001_mens_running_shoes_11550187236tiyyje6l87_prev_ui_3
        ),
        Products(
            id = "4",
            name = "Puma RS-X",
            price = "P520.00",
            originalPrice = "P600.00",
            category = "TRENDING",
            imageUrl = "",
            imageResId = R.drawable.nike_zoom_winflo_3_831561_001_mens_running_shoes_11550187236tiyyje6l87_prev_ui_3
        )
    )

    Scaffold(
        bottomBar = {
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
                    Row {
                        IconButton(onClick = { selected = 0 }) {
                            Icon(
                                painter = painterResource(id = R.drawable.home),
                                contentDescription = "Home",
                                tint = if (selected == 0) colorResource(R.color.Accent) else colorResource(R.color.black)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(onClick = { selected = 1 }) {
                            Icon(
                                painter = painterResource(id = R.drawable.favorite),
                                contentDescription = "Favorites",
                                tint = if (selected == 1) colorResource(R.color.Accent) else colorResource(R.color.black)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .offset(y = (-20).dp)
                            .size(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        FloatingActionButton(
                            onClick = { onCartClick() },
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

                    Row {
                        IconButton(onClick = { selected = 2 }) {
                            Icon(
                                painter = painterResource(id = R.drawable.notification),
                                contentDescription = "Notification",
                                tint = if (selected == 2) MaterialTheme.colorScheme.primary else Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(onClick = { selected = 3 }) {
                            Icon(
                                painter = painterResource(id = R.drawable.profile),
                                contentDescription = "Profile",
                                tint = if (selected == 3) MaterialTheme.colorScheme.primary else Color.Black
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(color = colorResource(R.color.Background))
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (selected == 0) {
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
                                onValueChange = {},
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

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when (selected) {
                    0 -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            item {
                                CategorySection(
                                    categories = categoriesState,
                                    selectedCategory = selectedCategory,
                                    onCategorySelected = { category ->
                                        selectedCategory = category
                                    }
                                )
                            }

                            item {
                                PopularSection(
                                    products = popularProducts,
                                    onProductClick = onProductClick,
                                    onFavoriteClick = { /* TODO */ }
                                )
                            }

                            item {
                                PromotionsSection()
                            }
                        }
                    }
                    1 -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Избранное",
                                style = Typography.headlineMedium
                            )
                        }
                    }
                    2 -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Уведомления",
                                style = Typography.headlineMedium
                            )
                        }
                    }
                    3 -> {
                        ProfileScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun CategorySection(
    categories: List<Categories>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Column {
        Text(
            text = stringResource(id = R.string.category),
            style = Typography.labelMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    category = category.name,
                    isSelected = selectedCategory == category.name,
                    onClick = { onCategorySelected(category.name) }
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable { onClick() }
            .clip(RoundedCornerShape(16.dp)),
        color = if (isSelected) colorResource(R.color.Accent) else colorResource(R.color.white),
        contentColor = if (isSelected) Color.White else Color.Black
    ) {
        Text(
            text = category,
            style = Typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun PopularSection(
    products: List<Products>,
    onProductClick: (Products) -> Unit,
    onFavoriteClick: (Products) -> Unit
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
                ProductCard(
                    product = product,
                    onProductClick = { onProductClick(product) },
                    onFavoriteClick = { onFavoriteClick(product) }
                )
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
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.summer_sale),
            contentScale = ContentScale.Crop,
            contentDescription = "sale"
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onProductClick = {},
        onCartClick = {},
        onSearchClick = {},
        onSettingsClick = {}
    )
}
