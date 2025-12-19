// ui/components/ProductCard.kt
package com.example.shoesshop.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoesshop.data.models.Products
import com.example.shoesshop.ui.theme.AppTypography
import com.example.shoesshop.ui.theme.Typography
import com.example.shoesshop.R

@Composable
fun ProductCard(
    product: Products,
    onProductClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onProductClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column {
            // Верхняя часть с изображением и кнопкой избранного
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                // Изображение товара
                if (product.imageResId != null) {
                    Image(
                        painter = painterResource(id = product.imageResId),
                        contentDescription = product.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(1f)   // было 0.95f
                            .aspectRatio(1.8f)  // было 2.1f
                    )

                } else {
                    // Запасной вариант, если нет изображения
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray.copy(alpha = 0.3f))
                    )
                }

                // Кнопка избранного поверх изображения
                IconButton(
                    onClick = {
                        isFavorite = !isFavorite
                        onFavoriteClick()
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = if (isFavorite) R.drawable.favorite_fill else R.drawable.favorite),
                        contentDescription = "Избранное",
                        tint = if (isFavorite) Color.Red else Color.Black
                    )
                }
            }

            // Нижняя часть с информацией о товаре
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .background(Color.White)
            ) {
                Text(
                    text = product.category,
                    style = Typography.displaySmall,
                    color = colorResource(R.color.Accent)
                )
                Text(
                    text = product.name,
                    style = Typography.bodySmall,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.price,
                        style = Typography.displayMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ProductCardPreview() {
    ProductCard(
        product = com.example.shoesshop.data.models.Products(
            id = "1",
            name = "Nike Air Max",
            price = "P752.00",
            originalPrice = "P850.00",
            category = "BEST SELLER",
            imageUrl = "",
            imageResId = R.drawable.nike_zoom_winflo_3_831561_001_mens_running_shoes_11550187236tiyyje6l87_prev_ui_3
        ),
        onProductClick = {},
        onFavoriteClick = {}
    )
}