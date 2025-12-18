package com.example.shoesshop.data.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoesshop.R
import com.example.shoesshop.data.viewModel.ResetPasswordOTPViewModel
import com.example.shoesshop.ui.components.AlertMessage
import com.example.shoesshop.ui.components.BackButton
import com.example.shoesshop.ui.components.InputTextBox
import com.example.shoesshop.ui.components.RegisterButton
import com.example.shoesshop.ui.theme.Typography

@Composable
fun ResetPasswordOTPScreen(
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordOTPViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onVerificationSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    var token by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.errorMessage != null) {
        AlertMessage(
            title = "Ошибка",
            description = uiState.errorMessage!!,
            onCancelClick = { viewModel.clearError() },
            onConfirmClick = { viewModel.clearError() }
        )
    }

    // затемнение и спиннер по всему экрану, как было
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

        // Индикация на месте кнопки: либо спиннер, либо RegisterButton
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterHorizontally),
                color = colorResource(R.color.Accent)
            )
        } else {
            RegisterButton(
                modifier = Modifier,
                text = stringResource(R.string.send),
                enabled = token.length == 6,
                onClick = {
                    viewModel.verifyOtp(
                        token = token,
                        context = context,
                        onSuccess = {
                            token = ""
                            onVerificationSuccess()
                        },
                        onError = { error ->
                            Log.e("ResetPasswordOTPScreen", "OTP error: $error")
                        }
                    )
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun ResetPasswordOTPScreenPreview() {
    ResetPasswordOTPScreen()
}
