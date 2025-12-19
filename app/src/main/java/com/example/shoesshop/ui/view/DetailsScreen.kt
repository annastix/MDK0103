package com.example.shoesshop.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoesshop.R
import com.example.shoesshop.data.ProductImages
import com.example.shoesshop.data.models.ProductDto
import com.example.shoesshop.data.models.Products
import com.example.shoesshop.data.service.CategoryNames
import com.example.shoesshop.ui.viewModel.DetailsUiState
import com.example.shoesshop.ui.viewModel.DetailsViewModel
import com.example.shoesshop.ui.viewModel.DetailsViewModelFactory
import com.example.shoesshop.ui.viewModel.FavouriteViewModel
import com.example.shoesshop.ui.theme.AppTypography
import com.example.shoesshop.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    productId: String,
    onBackClick: () -> Unit,
    viewModel: DetailsViewModel = viewModel(
        factory = DetailsViewModelFactory(productId)
    ),
    favouriteViewModel: FavouriteViewModel = viewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    val favouriteUiState by favouriteViewModel.uiState.collectAsStateWithLifecycle()
    val favouriteIds = favouriteUiState.products.map { it.id }.toSet()


    var showErrorDialog by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }

    if (state is DetailsUiState.Error && !showErrorDialog) {
        showErrorDialog = true
        errorText = state.message
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Ошибка") },
            text = { Text(errorText) }
        )
    }

    // флаг избранного: при первом запуске и при обновлении favouriteIds ставится по БД
    var isFavourite by remember { mutableStateOf(false) }
    LaunchedEffect(favouriteIds, productId) {
        isFavourite = favouriteIds.contains(productId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sneaker Shop",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* корзина */ }) {
                        Icon(
                            painter = painterResource(R.drawable.bag_2),
                            contentDescription = "Cart"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.Background)
                )
            )
        },
        bottomBar = {
            DetailsBottomBar(
                isFavourite = isFavourite,
                onFavouriteClick = {
                    val current = (state as? DetailsUiState.Success)?.product ?: return@DetailsBottomBar

                    val p = Products(
                        id = current.id,
                        name = current.title,
                        price = "₽${current.cost}",
                        originalPrice = "",
                        category = "",
                        imageUrl = "",
                        imageResId = ProductImages.forId(current.id)
                    )

                    favouriteViewModel.toggleFavourite(
                        context = context,
                        product = p,
                        currentlyFavourite = isFavourite
                    )
                    isFavourite = !isFavourite
                }
            )
        },
        containerColor = colorResource(R.color.Background)
    ) { padding ->
        when (state) {
            DetailsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is DetailsUiState.Success -> {
                DetailsContent(
                    product = state.product,
                    imageResId = state.imageResId,
                    modifier = Modifier.padding(padding)
                )
            }
            is DetailsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Не удалось загрузить товар")
                }
            }
        }
    }
}

@Composable
private fun DetailsContent(
    product: ProductDto,
    imageResId: Int,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    val categoryName = CategoryNames.nameFor(product.categoryId)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = product.title,
            style = Typography.headlineMedium
        )
        Text(
            text = categoryName,
            style = Typography.bodyMedium,
            color = colorResource(R.color.Hint)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "₽${product.cost}",
            style = Typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.stand_2),
                contentDescription = null,
                modifier = Modifier
                    .width(370.dp)
                    .height(110.dp)
                    .align(Alignment.BottomCenter)
            )

            Image(
                painter = painterResource(imageResId),
                contentDescription = product.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(400.dp)
                    .height(300.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items((0 until 5).toList()) { index ->
                val isSelected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(
                            width = 3.dp,
                            color = if (isSelected) colorResource(R.color.Accent) else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedIndex = index },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(imageResId),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(33.dp))

        Text(
            text = product.description,
            style = AppTypography.bodyMedium14,
            maxLines = if (expanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            color = colorResource(R.color.Text)
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = if (expanded) "Свернуть" else "Подробнее",
            style = AppTypography.bodyMedium14,
            color = colorResource(R.color.Accent),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .clickable { expanded = !expanded }
        )
    }
}

@Composable
private fun DetailsBottomBar(
    isFavourite: Boolean,
    onFavouriteClick: () -> Unit
) {
    Surface(
        tonalElevation = 8.dp,
        color = colorResource(R.color.Background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .padding(bottom = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(colorResource(R.color.SubTextLight))
                    .clickable { onFavouriteClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isFavourite) R.drawable.favorite_fill else R.drawable.favorite
                    ),
                    contentDescription = "Favorite",
                    tint = if (isFavourite) Color.Red else colorResource(R.color.Text)
                )
            }

            Button(
                onClick = { /* добавить в корзину */ },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.Accent)
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.bag_2),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "В корзину",
                    style = AppTypography.bodyMedium14
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailsScreenPreview() {
    val mockProduct = ProductDto(
        id = "21c2d7c0-a2ea-49a2-9198-52bafafc6958",
        title = "Nike Air Max 270 Essential",
        categoryId = "outdoor",
        cost = 179.39,
        description = "Вставка Max Air 270 обеспечивает непревзойдённый комфорт в течение всего дня. " +
                "Изящный дизайн и лёгкие материалы делают эту модель идеальной для повседневной носки.",
        isBestSeller = true
    )

    val imageResId = ProductImages.forId(mockProduct.id)

    Scaffold(
        containerColor = colorResource(R.color.Background),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sneaker Shop",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontFamily = Typography.titleSmall.fontFamily,
                        fontWeight = Typography.titleSmall.fontWeight,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(colorResource(R.color.white))
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(colorResource(R.color.white))
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.bag_2),
                            contentDescription = "Cart"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.Background)
                )
            )
        },
        bottomBar = {
            DetailsBottomBar(
                isFavourite = true,
                onFavouriteClick = {}
            )
        }
    ) { padding ->
        DetailsContent(
            product = mockProduct,
            imageResId = imageResId,
            modifier = Modifier.padding(padding)
        )
    }
}
