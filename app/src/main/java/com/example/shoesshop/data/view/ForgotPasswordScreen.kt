package com.example.shoesshop.data.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoesshop.R
import com.example.shoesshop.data.PreferenceHelper
import com.example.shoesshop.data.viewModel.ForgotPasswordViewModel
import com.example.shoesshop.data.viewModel.PasswordRecoveryState
import com.example.shoesshop.ui.components.AlertMessage
import com.example.shoesshop.ui.components.BackButton
import com.example.shoesshop.ui.components.InputTextBox
import com.example.shoesshop.ui.components.RegisterButton
import com.example.shoesshop.ui.theme.Typography

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ForgotPasswordViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onEmailSaved: () -> Unit = {}
) {
    val context = LocalContext.current
    val email by viewModel.email.collectAsState()
    val isValid by viewModel.isEmailValid.collectAsState()
    val state by viewModel.state.collectAsState()

    if (state is PasswordRecoveryState.Error) {
        val msg = (state as PasswordRecoveryState.Error).message
        AlertMessage(
            title = "Ошибка",
            description = msg,
            onCancelClick = { viewModel.resetState() },
            onConfirmClick = { viewModel.resetState() }
        )
    }

    if (state is PasswordRecoveryState.Success) {
        val msg = (state as PasswordRecoveryState.Success).message
        AlertMessage(
            title = "Готово",
            description = msg,
            onCancelClick = {
                viewModel.resetState()
                PreferenceHelper.saveResetEmail(context, email.trim())
                onEmailSaved()
            },
            onConfirmClick = {
                viewModel.resetState()
                PreferenceHelper.saveResetEmail(context, email.trim())
                onEmailSaved()
            }
        )
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
                text = stringResource(R.string.forgot),
                style = Typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.reset_password),
                style = Typography.bodySmall,
                modifier = Modifier
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(bottom = 40.dp),
                color = colorResource(R.color.SubTextDark)
            )
        }

        InputTextBox(
            modifier = Modifier.padding(bottom = 40.dp),
            value = email,
            onValueChange = { viewModel.updateEmail(it) },
            placeholder = "xyz@gmail.co"
        )

        // Кнопка с индикатором загрузки
        if (state is PasswordRecoveryState.Loading) {
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
                enabled = isValid,
                onClick = {
                    viewModel.recoverPassword()
                }
            )
        }

        Spacer(modifier = Modifier.weight(4f))
    }
}

@Preview
@Composable
private fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen()
}
