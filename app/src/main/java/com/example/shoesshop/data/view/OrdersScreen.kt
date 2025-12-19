package com.example.shoesshop.data.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoesshop.R
import com.example.shoesshop.data.ProductImages
import com.example.shoesshop.data.models.OrderHistoryDto
import com.example.shoesshop.data.models.OrderItemRequest
import com.example.shoesshop.data.viewModel.OrdersViewModel
import com.example.shoesshop.ui.theme.Typography
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    userId: String,
    onBackClick: () -> Unit,
    ordersViewModel: OrdersViewModel = viewModel()
) {
    val uiState by ordersViewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        ordersViewModel.loadOrders(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.Background)
                ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.orders),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(colorResource(R.color.Background))
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
                        Text(text = uiState.errorMessage!!)
                    }
                }

                else -> {
                    val grouped = groupOrders(uiState.orders)

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp)
                    ) {
                        grouped.forEach { (header, list) ->
                            item {
                                Text(
                                    text = header,
                                    style = Typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                            items(list) { order ->
                                OrderItemRow(order = order)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun groupOrders(orders: List<OrderHistoryDto>): Map<String, List<OrderHistoryDto>> {
    if (orders.isEmpty()) return emptyMap()

    val now = OffsetDateTime.now(ZoneId.systemDefault())
    val today: LocalDate = now.toLocalDate()
    val yesterday = today.minusDays(1)
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

    return orders.groupBy { order ->
        val created = order.created_at?.let {
            runCatching { OffsetDateTime.parse(it) }.getOrNull()
        } ?: return@groupBy "Другое"

        val date = created.toLocalDate()
        when {
            date.isEqual(today) -> stringResource(R.string.recent)   // "Недавний"
            date.isEqual(yesterday) -> stringResource(R.string.yesterday)
            else -> formatter.format(date)
        }
    }
}

@Composable
private fun formatOrderTimeText(createdAt: String?): String {
    val created = createdAt?.let {
        runCatching { OffsetDateTime.parse(it) }.getOrNull()
    } ?: return ""

    val now = OffsetDateTime.now(ZoneId.systemDefault())
    val today = now.toLocalDate()

    return if (created.toLocalDate().isEqual(today)) {
        val minutes = ChronoUnit.MINUTES
            .between(created, now)
            .coerceAtLeast(0)
        "$minutes " + stringResource(R.string.min_ago) // "мин назад"
    } else {
        created.toLocalTime()
            .truncatedTo(ChronoUnit.MINUTES)
            .format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}

@Composable
private fun OrderItemRow(order: OrderHistoryDto) {
    val firstItem: OrderItemRequest? = order.orders_items?.firstOrNull()

    val total = firstItem?.coast ?: 0.0
    val discount = if (total > 0) total * 0.1 else 0.0
    val timeText = formatOrderTimeText(order.created_at)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.white)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Картинка
            val imageRes = ProductImages.forId(firstItem?.product_id ?: "")
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = firstItem?.title,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.Crop
            )

            // Центральная колонка: номер + название + цены
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // верхняя строка: номер заказа
                Text(
                    text = "№ ${order.id}",
                    style = Typography.bodySmall,
                    color = colorResource(R.color.Accent),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(2.dp))

                // название в 2 строки
                Text(
                    text = firstItem?.title ?: "Товар",
                    style = Typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(8.dp))

                // нижняя строка: сумма и скидка как на макете
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₽${"%.2f".format(total)}",
                        style = Typography.titleSmall
                    )
                    if (discount > 0) {
                        Spacer(Modifier.width(16.dp))
                        Text(
                            text = "₽${"%.2f".format(discount)}",
                            style = Typography.titleSmall,
                            color = colorResource(R.color.Hint)
                        )
                    }
                }
            }

            // Правая колонка: только время, мелко
            if (timeText.isNotBlank()) {
                Text(
                    text = timeText,
                    style = Typography.titleSmall,
                    color = colorResource(R.color.Hint),
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

