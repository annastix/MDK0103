package com.example.shoesshop.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoesshop.ui.components.BackButton
import com.example.shoesshop.R
import com.example.shoesshop.ui.viewModel.RegisterAccountViewModel
import com.example.shoesshop.ui.components.AlertMessage
import com.example.shoesshop.ui.components.InputTextBox
import com.example.shoesshop.ui.components.PasswordInputTextBox
import com.example.shoesshop.ui.components.RegisterButton
import com.example.shoesshop.ui.theme.Typography

@Composable
fun RegisterAccount(
    onNavigateToSignIn: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onSendOTP: () -> Unit = {},
    viewModel: RegisterAccountViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Флаг для показа/скрытия диалога
    var showErrorDialog by remember { mutableStateOf(false) }
    var currentErrorMessage by remember { mutableStateOf<String?>(null) }

    // Обновляем диалог при изменении ошибки в состоянии
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null && !showErrorDialog) {
            currentErrorMessage = uiState.errorMessage
            showErrorDialog = true
        }
    }

    // Диалог ошибки
    if (showErrorDialog && currentErrorMessage != null) {
        AlertMessage(
            title = "Ошибка",
            description = currentErrorMessage!!,
            onCancelClick = {
                showErrorDialog = false
                currentErrorMessage = null
                viewModel.clearError()
            },
            onConfirmClick = {
                showErrorDialog = false
                currentErrorMessage = null
                viewModel.clearError()
            }
        )
    }


    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPolicyAccepted by remember { mutableStateOf(false) }

    // ФИКС: Правильная логика валидации формы
    val isFormValid = remember(name, email, password, isPolicyAccepted) {
        name.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                isPolicyAccepted
    }

    Column(
        modifier = Modifier
            .background(colorResource(R.color.white))
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        BackButton(onClick = onBackClick)
        Spacer(modifier = Modifier.height(21.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.register),
                style = Typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.details),
                style = Typography.bodySmall,
                modifier = Modifier.padding(bottom = 54.dp),
                color = colorResource(R.color.SubTextDark)
            )
        }

        Text(
            text = stringResource(R.string.name),
            style = Typography.labelMedium,
            modifier = Modifier.padding(bottom = 12.dp),
            color = colorResource(R.color.Text)
        )

        InputTextBox(
            modifier = Modifier,
            value = name,
            onValueChange = { name = it },
            placeholder = "xxxxxxxx"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.email),
            style = Typography.labelMedium,
            modifier = Modifier.padding(bottom = 12.dp),
            color = colorResource(R.color.Text)
        )

        InputTextBox(
            modifier = Modifier,
            value = email,
            onValueChange = { email = it },
            placeholder = "xyz@gmail.com"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.password),
            style = Typography.labelMedium,
            modifier = Modifier.padding(bottom = 12.dp),
            color = colorResource(R.color.Text)
        )

        PasswordInputTextBox(
            modifier = Modifier,
            value = password,
            onValueChange = { password = it },
            placeholder = "........"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ФИКС: Правильная обработка чекбокса
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(MaterialTheme.shapes.small)
                    .clickable { isPolicyAccepted = !isPolicyAccepted }
                    .border(
                        width = 2.dp,
                        color = if (isPolicyAccepted)
                            colorResource(R.color.Accent)
                        else MaterialTheme.colorScheme.outlineVariant,
                        shape = MaterialTheme.shapes.small
                    )
                    .background(
                        if (isPolicyAccepted) colorResource(R.color.Accent)
                        else Color.Transparent
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isPolicyAccepted) {
                    Icon(
                        painter = painterResource(id = R.drawable.policy_check),
                        contentDescription = "Выбрано",
                        modifier = Modifier.size(16.dp),
                        tint = colorResource(R.color.black)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.personal_data),
                style = Typography.labelMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ФИКС: Кнопка правильно вызывает регистрацию
        RegisterButton(
            modifier = Modifier,
            text = if (uiState.isLoading) stringResource(R.string.sign_up)+"..." else stringResource(R.string.sign_up),
            onClick = {
                if (!uiState.isLoading && isFormValid) {
                    viewModel.register(
                        context = context,
                        name = name,
                        email = email,
                        password = password,
                        isPolicyAccepted = isPolicyAccepted,
                        onSuccess = onSendOTP
                    )
                }
            },
            enabled = name.isNotBlank() &&
                    email.isNotBlank() &&
                    password.isNotBlank() &&
                    isPolicyAccepted &&
                    !uiState.isLoading // Блокируем при загрузке
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 47.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            TextButton(
                onClick = onNavigateToSignIn,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = colorResource(R.color.Hint),
                                fontFamily = Typography.bodySmall.fontFamily,
                                fontSize = Typography.bodySmall.fontSize
                            )
                        ) {
                            append(stringResource(id = R.string.already_account))
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = colorResource(R.color.Text),
                                fontFamily = Typography.bodySmall.fontFamily,
                                fontSize = Typography.bodySmall.fontSize,
                            )
                        ) {
                            append(stringResource(id = R.string.enter_to_account))
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun RegisterAccountPreview() {
    val viewModel: RegisterAccountViewModel = viewModel()
    RegisterAccount({}, {}, {}, viewModel)
}