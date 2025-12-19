// ui/components/ProductCard.kt
package com.example.shoesshop.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoesshop.R
import com.example.shoesshop.data.models.Products
import com.example.shoesshop.ui.theme.Typography

@Composable
fun ProductCard(
    product: Products,
    isFavorite: Boolean,
    onProductClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onProductClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                if (product.imageResId != 0) {
                    Image(
                        painter = painterResource(id = product.imageResId),
                        contentDescription = product.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .aspectRatio(1.8f)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray.copy(alpha = 0.3f))
                    )
                }

                // Кнопка "избранное"
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isFavorite)
                                R.drawable.favorite_fill
                            else
                                R.drawable.favorite
                        ),
                        contentDescription = "Избранное",
                        tint = if (isFavorite) Color.Red else Color.Black
                    )
                }
            }

            // Низ: текст + "врезанный" плюс
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(
                            start = 12.dp,
                            top = 4.dp,
                            end = 56.dp, // отступ под кнопку справа
                            bottom = 12.dp
                        )
                ) {
                    Text(
                        text = if (product.category.isNotEmpty()) product.category else "",
                        style = Typography.displaySmall,
                        color = colorResource(R.color.Accent)
                    )
                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = product.name,
                        style = Typography.bodySmall,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.price,
                        style = Typography.displayMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // "Врезанный" плюс в правом нижнем углу
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(40.dp)
                        .background(
                            color = colorResource(R.color.Accent),
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 0.dp,
                                bottomEnd = 0.dp,
                                bottomStart = 0.dp
                            )
                        )
                        .clickable { onAddClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add),
                        contentDescription = "Добавить",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    ProductCard(
        product = Products(
            id = "1",
            name = "Nike Air Max",
            price = "₽752.00",
            originalPrice = "₽850.00",
            category = "BEST SELLER",
            imageUrl = "",
            imageResId = R.drawable.nike_zoom_winflo_3_831561_001_mens_running_shoes_11550187236tiyyje6l87_prev_ui_3,
        ),
        isFavorite = true,
        onProductClick = {},
        onFavoriteClick = {},
        onAddClick = {}
    )
}
