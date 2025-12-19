package com.example.shoesshop.data.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoesshop.R
import com.example.shoesshop.data.viewModel.CartViewModel
import com.example.shoesshop.ui.theme.AppTypography
import com.example.shoesshop.ui.theme.Typography
import kotlin.math.roundToInt
import androidx.compose.ui.platform.LocalDensity


data class CartItemUi(
    val cartId: String,
    val title: String,
    val price: Double,
    val imageResId: Int,
    val count: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCartScreen(
    onBackClick: () -> Unit = {},
    viewModel: CartViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCart(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.Background)
                ),
                modifier = Modifier.padding(start = 14.dp),
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
                            modifier = Modifier
                                .size(44.dp)
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
        bottomBar = { CartSummaryBar(uiState.items) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.errorMessage ?: "",
                            style = Typography.bodyMedium,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    Text(
                        text = "${uiState.items.size} " + stringResource(R.string.items),
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
                        items(uiState.items, key = { it.cartId }) { item ->
                            CartSwipeItem(
                                item = item,
                                onIncrease = { viewModel.increase(item.cartId) },
                                onDecrease = { viewModel.decrease(item.cartId) },
                                onDelete = { viewModel.remove(item.cartId) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Свой swipe‑layout:
 * - offsetX = 0f      → карточка по центру;
 * - offsetX = +panel  → открыт блок + / - слева;
 * - offsetX = -panel  → открыт блок удаления справа.
 */
@Composable
private fun CartSwipeItem(
    item: CartItemUi,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onDelete: () -> Unit
) {
    val panelWidth = 88.dp
    val density = LocalDensity.current
    val panelWidthPx = with(density) { panelWidth.toPx() }

    var offsetX by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
    ) {
        Row(
            modifier = Modifier.matchParentSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(panelWidth)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorResource(R.color.Accent)),
                contentAlignment = Alignment.Center
            ) {
                CartCountButtons(
                    count = item.count,
                    onIncrease = onIncrease,
                    onDecrease = onDecrease
                )
            }

            Box(
                modifier = Modifier
                    .width(panelWidth)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFF5A5A)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(R.drawable.trash),
                        contentDescription = "Удалить",
                        tint = Color.White
                    )
                }
            }
        }

        CartItemCard(
            item,
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            offsetX = when {
                                offsetX > panelWidthPx / 2 -> panelWidthPx
                                offsetX < -panelWidthPx / 2 -> -panelWidthPx
                                else -> 0f
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        val newX = offsetX + dragAmount
                        offsetX = newX.coerceIn(-panelWidthPx, panelWidthPx)
                    }
                }
        )
    }
}


@Composable
private fun CartItemCard(
    item: CartItemUi,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.white)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(9.dp)
        ) {
            Image(
                painter = if (item.imageResId != 0)
                    painterResource(item.imageResId)
                else painterResource(R.drawable.nike_zoom_winflo_3_831561_001_mens_running_shoes_11550187236tiyyje6l87_prev_ui_3),
                contentDescription = item.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorResource(R.color.Background))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = Typography.bodySmall
                )
                Text(
                    text = "₽${item.price}",
                    style = Typography.bodySmall,
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
                .padding(horizontal = 16.dp, vertical = 40.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.sum),
                    style = AppTypography.bodyMedium14,
                    color = colorResource(R.color.SubTextDark)
                )
                Text("₽${"%.2f".format(subtotal)}", style = AppTypography.bodyMedium14)
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.delivery),
                    style = AppTypography.bodyMedium14,
                    color = colorResource(R.color.SubTextDark)
                )
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
                Text(stringResource(R.string.total_cost), style = Typography.bodyLarge)
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
                Text(stringResource(R.string.create_order), style = AppTypography.bodyMedium14)
            }
        }
    }
}

@Composable
fun DashedDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.Gray,
    thickness: Dp = 1.dp,
    dashLength: Dp = 8.dp,
    dashGap: Dp = 4.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val y = size.height / 2
            var x = 0f
            while (x < size.width) {
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
    MyCartScreen()
}
