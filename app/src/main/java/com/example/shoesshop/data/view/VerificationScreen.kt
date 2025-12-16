package com.example.shoesshop.data.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoesshop.data.viewModel.VerificationViewModel
import com.example.shoesshop.ui.components.BackButton
import com.example.shoesshop.R
import com.example.shoesshop.ui.components.AlertMessage
import com.example.shoesshop.ui.components.InputTextBox
import com.example.shoesshop.ui.components.RegisterButton
import com.example.shoesshop.ui.theme.Typography

@Composable
fun VerificationScreen(
    modifier: Modifier = Modifier,
    viewModel: VerificationViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onVerificationSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    var token by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    val userEmail by remember {
        mutableStateOf(viewModel.getStoredEmail(context))
    }

    // Показываем AlertDialog при наличии ошибки
    if (uiState.errorMessage != null) {
        AlertMessage(
            title = "Ошибка",
            description = uiState.errorMessage!!,
            onCancelClick = { viewModel.clearError() },
            onConfirmClick = { viewModel.clearError() }
        )
    }

    // Показываем индикатор загрузки
    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = colorResource(R.color.Accent))
        }
    }

    Column(
        modifier = Modifier
            .background(colorResource(R.color.white))
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.weight(0.2f))

        // Верхняя часть с кнопкой назад
        BackButton(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.otp_verification),
                style = Typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.check_email_please),
                style = Typography.bodySmall,
                color = colorResource(R.color.SubTextDark),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.otp_code),
            style = Typography.labelMedium,
            modifier = Modifier.padding(bottom = 12.dp),
            color = colorResource(R.color.Text)
        )

        InputTextBox(
            modifier = Modifier,
            value = token,
            onValueChange = { token = it }
        )

        Spacer(modifier = Modifier.weight(0.2f))

        RegisterButton(
            modifier = Modifier,
            text = stringResource(R.string.send),
            onClick = {
                if (token.length != 6) {
                    return@RegisterButton
                }

                viewModel.verifyOTP(
                    token = token,
                    context = context,
                    onSuccess = {
                        token = ""
                        onVerificationSuccess()
                    },
                    onError = { error ->

                    }
                )
            },
            enabled = token.length == 6
        )

        Spacer(modifier = Modifier.weight(1f))

        // Кнопка повторной отправки OTP
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(
                onClick = {
                    Toast.makeText(context, "Запрос на повторную отправку OTP", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(
                    text = "Отправить код повторно",
                    color = colorResource(R.color.Accent),
                    style = Typography.bodyMedium
                )
            }
        }
    }
}
@Preview
@Composable
private fun VerificationScreenPreview() {
    VerificationScreen()
}