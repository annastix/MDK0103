package com.example.shoesshop.data.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoesshop.R
import com.example.shoesshop.data.viewModel.CartViewModel
import com.example.shoesshop.data.viewModel.CheckoutViewModel
import com.example.shoesshop.ui.components.OrderSuccessDialog
import com.example.shoesshop.ui.theme.Typography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit,
    onBackToHome: () -> Unit,
    userId: String?,                     // из auth
    paymentId: String?,                  // uuid карты, если есть
    totalAmount: Double,
    deliveryPrice: Double,
    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CheckoutViewModel(cartViewModel) as T
            }
        }
    )
) {
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("emmanuel@gmail.com") }
    var phone by remember { mutableStateOf("+234-811-732-5298") }

    var isEditingEmail by remember { mutableStateOf(false) }
    var isEditingPhone by remember { mutableStateOf(false) }
    var isEditingAddress by remember { mutableStateOf(false) }

    val addressFromProfile = "1082 Аэропорт, Нигерии"
    val addressFromGeo = "Текущая геопозиция"
    val isLocationEnabled by remember { mutableStateOf(false) }
    var address by remember {
        mutableStateOf(
            if (isLocationEnabled) addressFromGeo else addressFromProfile
        )
    }

    var isDialogOpen by remember { mutableStateOf(false) }

    val sum = totalAmount
    val delivery = deliveryPrice
    val total = sum + delivery

    Scaffold(
        containerColor = colorResource(R.color.Background),
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(start = 21.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.Background)
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.cart),
                        modifier = Modifier.fillMaxWidth(),
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
                            contentDescription = stringResource(R.string.back_to_shopping),
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
            // Контактная информация
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.white)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.contact_inform),
                        style = Typography.labelMedium
                    )

                    // Email
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.email),
                            contentDescription = null,
                            tint = colorResource(R.color.Text),
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(colorResource(R.color.Background))
                                .padding(10.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            if (isEditingEmail) {
                                OutlinedTextField(
                                    value = email,
                                    onValueChange = { email = it },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                Text(text = email, style = Typography.bodyMedium)
                            }
                            Text(
                                text = stringResource(R.string.email),
                                style = Typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        IconButton(
                            onClick = { isEditingEmail = !isEditingEmail }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.edit),
                                contentDescription = stringResource(R.string.email),
                                tint = colorResource(R.color.Text)
                            )
                        }
                    }

                    // Phone
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.call),
                            contentDescription = null,
                            tint = colorResource(R.color.Text),
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(colorResource(R.color.Background))
                                .padding(10.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            if (isEditingPhone) {
                                OutlinedTextField(
                                    value = phone,
                                    onValueChange = { phone = it },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                Text(text = phone, style = Typography.bodyMedium)
                            }
                            Text(
                                text = stringResource(R.string.phone),
                                style = Typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        IconButton(
                            onClick = { isEditingPhone = !isEditingPhone }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.edit),
                                contentDescription = stringResource(R.string.phone),
                                tint = colorResource(R.color.Text)
                            )
                        }
                    }

                    // Адрес
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.address),
                                style = Typography.bodyMedium,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            if (isEditingAddress) {
                                OutlinedTextField(
                                    value = address,
                                    onValueChange = { address = it },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                Text(
                                    text = address,
                                    style = Typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                        IconButton(
                            onClick = { isEditingAddress = !isEditingAddress }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.edit),
                                contentDescription = stringResource(R.string.address),
                                tint = colorResource(R.color.Text)
                            )
                        }
                    }


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)                 // нужная высота карты
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.map),
                            contentDescription = stringResource(R.string.map), // или Fit/FillBounds под макет
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(
                        text = stringResource(R.string.payment_method),
                        style = Typography.bodyMedium
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(colorResource(R.color.Background))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.card),
                            contentDescription = null,
                            tint = colorResource(R.color.Text)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "DbL Card", style = Typography.bodyMedium)
                            Text(
                                text = "**** **** 0696 4629",
                                style = Typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_down),
                            contentDescription = null,
                            tint = colorResource(R.color.Text)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Суммы
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.sum), style = Typography.bodyMedium)
                    Text(text = "₽${"%.2f".format(sum)}", style = Typography.bodyMedium)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.delivery), style = Typography.bodyMedium)
                    Text(text = "₽${"%.2f".format(delivery)}", style = Typography.bodyMedium)
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = Color.LightGray
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.total_cost), style = Typography.bodyMedium)
                    Text(
                        text = "₽${"%.2f".format(total)}",
                        style = Typography.bodyMedium,
                        color = colorResource(R.color.Accent)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { isDialogOpen = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.Accent)
                )
            ) {
                Text(
                    text = stringResource(R.string.confirm),
                    style = Typography.labelMedium,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(60.dp))
        }

        // Диалог подтверждения
        if (isDialogOpen) {
            OrderSuccessDialog(
                visible = isDialogOpen,
                onDismiss = { if (!checkoutViewModel.isSaving) isDialogOpen = false },
                onBackToHome = onBackToHome,
                isSaving = checkoutViewModel.isSaving,
                onConfirmClick = {
                    if (userId != null) {
                        checkoutViewModel.saveOrder(
                            email = email,
                            phone = phone,
                            address = address,
                            deliveryCoast = deliveryPrice.toLong(),
                            userId = userId,
                            paymentId = paymentId
                        )
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CheckoutScreenPreview() {
    CheckoutScreen(
        onBackClick = {},
        onBackToHome = {},
        userId = null,
        paymentId = null,
        totalAmount = 753.95,
        deliveryPrice = 60.20,
        cartViewModel = CartViewModel()
    )
}