package com.example.shoesshop.data.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.shoesshop.R
import com.example.shoesshop.ui.theme.AppTypography
import com.example.shoesshop.ui.theme.Typography

data class CartItemUi(
    val cartId: String,
    val title: String,
    val price: Double,
    val imageResId: Int,
    val count: Int
)

sealed interface CartUiState {
    data object Loading : CartUiState
    data class Success(val items: List<CartItemUi>) : CartUiState
    data class Error(val message: String) : CartUiState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenSimple(
    onBackClick: () -> Unit = {}
) {
    val items = remember {
        mutableStateListOf(
            CartItemUi("1", "Nike Club Max", 584.95, R.drawable.nike_zoom_winflo_3_831561_001_mens_running_shoes_11550187236tiyyje6l87_prev_ui_3, 1),
            CartItemUi("2", "Nike Air Max 200", 94.05, R.drawable.nike_zoom_winflo_3_831561_001_mens_running_shoes_11550187236tiyyje6l87_prev_ui_3, 1),
            CartItemUi("3", "Nike Air Max 270 Essential", 74.95, R.drawable.nike_zoom_winflo_3_831561_001_mens_running_shoes_11550187236tiyyje6l87_prev_ui_3, 1)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.Background)
                ),
                modifier = Modifier.padding(start = 21.dp),
                title = {
                    Text(
                        text = stringResource(R.string.cart),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            modifier = Modifier.size(44.dp)
                                .background(
                                    color = colorResource(R.color.white),
                                    shape = CircleShape
                                ),
                            painter = painterResource(R.drawable.back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        containerColor = colorResource(R.color.Background),
        bottomBar = {
            CartSummaryBar(items)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(
                text = "${items.size} "+stringResource(R.string.items),
                style = Typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items, key = { it.cartId }) { item ->
                    CartItemSwipeableM3(
                        item = item,
                        onIncrease = {
                            val index = items.indexOfFirst { it.cartId == item.cartId }
                            if (index != -1) {
                                val old = items[index]
                                items[index] = old.copy(count = old.count + 1)
                            }
                        },
                        onDecrease = {
                            val index = items.indexOfFirst { it.cartId == item.cartId }
                            if (index != -1 && items[index].count > 1) {
                                val old = items[index]
                                items[index] = old.copy(count = old.count - 1)
                            }
                        },
                        onDelete = {
                            items.removeAll { it.cartId == item.cartId }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartItemSwipeableM3(
    item: CartItemUi,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onDelete: () -> Unit
) {
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
            }
            false
        }
    )

    SwipeToDismissBox(
        state = state,
        backgroundContent = {
            when (state.targetValue) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    CartCountButtons(
                        count = item.count,
                        onIncrease = onIncrease,
                        onDecrease = onDecrease
                    )
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFF5A5A))
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.trash),
                                contentDescription = "Удалить",
                                tint = Color.White
                            )
                        }
                    }
                }
                SwipeToDismissBoxValue.Settled -> {}
            }
        },
        content = {
            CartItemCard(item)
        }
    )
}

@Composable
fun CartItemCard(item: CartItemUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.white)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = painterResource(item.imageResId),
                contentDescription = item.title,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = Typography.bodyMedium
                )
                Text(
                    text = "₽${item.price}",
                    style = Typography.bodyMedium,
                    color = colorResource(R.color.Text)
                )
            }
        }
    }
}

@Composable
fun CartCountButtons(
    count: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(R.color.Accent)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onIncrease) {
            Icon(
                painter = painterResource(R.drawable.add),
                contentDescription = "+",
                tint = Color.White
            )
        }
        Text(
            text = count.toString(),
            color = Color.White,
            style = AppTypography.bodyMedium14
        )
        IconButton(onClick = onDecrease) {
            Icon(
                painter = painterResource(R.drawable.minus),
                contentDescription = "-",
                tint = Color.White
            )
        }
    }
}

@Composable
fun CartSummaryBar(items: List<CartItemUi>) {
    val subtotal = items.sumOf { it.price * it.count }
    val delivery = if (items.isEmpty()) 0.0 else 60.20
    val total = subtotal + delivery

    Surface(
        tonalElevation = 4.dp,
        color = colorResource(R.color.white)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Сумма", style = AppTypography.bodyMedium14)
                Text("₽${"%.2f".format(subtotal)}", style = AppTypography.bodyMedium14)
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Доставка", style = AppTypography.bodyMedium14)
                Text("₽${"%.2f".format(delivery)}", style = AppTypography.bodyMedium14)
            }
            Spacer(modifier = Modifier.height(8.dp))
            DashedDivider(
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 10.dp),
                color = colorResource(R.color.SubTextDark),
                thickness = 2.dp,
                dashLength = 10.dp,
                dashGap = 5.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Итого", style = Typography.bodyLarge)
                Text(
                    "₽${"%.2f".format(total)}",
                    style = Typography.bodyLarge,
                    color = colorResource(R.color.Accent)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.Accent)
                )
            ) {
                Text("Оформить заказ", style = AppTypography.bodyMedium14)
            }
        }
    }
}

@Composable
fun DashedDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.Gray,
    thickness: Dp = 1.dp,
    dashLength: Dp = 8.dp, // Длина штриха
    dashGap: Dp = 4.dp // Длина пробела
) {
    Box(modifier = modifier.fillMaxWidth().height(thickness)) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val y = size.height / 2
            var x = 0f
            while (x < size.width) {
                // Отрисовка штриха
                drawLine(
                    color = color,
                    start = Offset(x, y),
                    end = Offset(x + dashLength.toPx(), y),
                    strokeWidth = thickness.toPx()
                )
                x += dashLength.toPx() + dashGap.toPx()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CartScreenPreview() {
    CartScreenSimple()
}