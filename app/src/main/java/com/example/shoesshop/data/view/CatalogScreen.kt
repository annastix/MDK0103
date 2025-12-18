//package com.example.shoesshop.data.view
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.FavoriteBorder
//import androidx.compose.material.icons.filled.ShoppingCart
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.shoesshop.R
//import com.example.shoesshop.data.models.ProductDto
//import com.example.shoesshop.data.viewModel.CatalogViewModel
//import java.util.UUID
//
//@Composable
//fun CatalogScreen(
//    modifier: Modifier = Modifier,
//    viewModel: CatalogViewModel = viewModel(),
//    onBackClick: () -> Unit = {}
//) {
//    val state by viewModel.uiState.collectAsState()
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(colorResource(R.color.white))
//    ) {
//        if (state.isLoading) {
//            CircularProgressIndicator(
//                modifier = Modifier.align(Alignment.Center),
//                color = colorResource(R.color.Accent)
//            )
//        } else {
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(2),
//                contentPadding = PaddingValues(16.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp),
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                items(state.products) { product ->
//                    CatalogItemCard(
//                        product = product,
//                        isFavorite = state.favouriteIds.contains(product.id),
//                        onToggleFavorite = { viewModel.toggleFavourite(product.id) },
//                        onAddToCart = { /* TODO */ }
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun CatalogItemCard(
//    product: ProductDto,
//    isFavorite: Boolean,
//    onToggleFavorite: () -> Unit,
//    onAddToCart: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .height(200.dp)
//            .fillMaxWidth(),
//        colors = CardDefaults.cardColors(
//            containerColor = colorResource(R.color.Background)
//        ),
//        elevation = CardDefaults.cardElevation(2.dp)
//    ) {
//        Box(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(12.dp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    // тут могла бы быть картинка товара
//                    Spacer(modifier = Modifier.width(1.dp))
//
//                    IconButton(onClick = onToggleFavorite) {
//                        Icon(
//                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.FavoriteBorder,
//                            contentDescription = null,
//                            tint = if (isFavorite) Color.Red else colorResource(R.color.SubTextDark)
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                if (product.isBestSeller) {
//                    Text(
//                        text = "BEST SELLER",
//                        color = colorResource(R.color.Accent),
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//
//                Text(
//                    text = product.title,
//                    color = colorResource(R.color.Text),
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(
//                    text = "₽${product.cost.toInt()}",
//                    color = colorResource(R.color.Text)
//                )
//            }
//
//            IconButton(
//                onClick = onAddToCart,
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .padding(8.dp)
//                    .size(40.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.ShoppingCart,
//                    contentDescription = null,
//                    tint = Color.White
//                )
//            }
//        }
//    }
//}
