package com.example.shoesshop.data.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoesshop.R
import com.example.shoesshop.data.models.Products
import com.example.shoesshop.data.viewModel.FavouriteUiState
import com.example.shoesshop.data.viewModel.FavouriteViewModel
import com.example.shoesshop.ui.components.ProductCard
import com.example.shoesshop.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    onBackClick: () -> Unit,
    onProductClick: (Products) -> Unit,
    viewModel: FavouriteViewModel = viewModel()
) {
    val uiState: FavouriteUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadFavourites(context)
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
                        text = stringResource(R.string.favourite),
                        modifier = Modifier.width(291.dp),
                        fontFamily = Typography.titleSmall.fontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
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
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colorResource(R.color.Accent))
                }
            } else if (uiState.products.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.favourite),
                        style = Typography.bodySmall,
                        color = Color.Gray
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(uiState.products) { product ->
                        ProductCard(
                            product = product,
                            onProductClick = { onProductClick(product) },
                            onFavoriteClick = {
                                viewModel.toggleFavourite(
                                    context = context,
                                    product = product,
                                    currentlyFavourite = true
                                )
                            },
                            isFavorite = true
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun FavoriteScreenPreview() {
    // заглушка: пустой список избранного
    FavoriteScreen(onBackClick = {}, onProductClick = {})
}
