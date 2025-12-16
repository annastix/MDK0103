package com.example.shoesshop.data.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoeshop.data.viewModel.VerificationViewModel
import com.example.shoeshop.ui.components.BackButton
import com.example.shoesshop.R
import com.example.shoesshop.ui.components.InputTextBox
import com.example.shoesshop.ui.components.RegisterButton
import com.example.shoesshop.ui.theme.Typography


@Composable
fun VerificationScreen(
    modifier: Modifier = Modifier,
    viewModel: VerificationViewModel = viewModel(),
    onBackClick: () -> Unit = {},

) {

    val context = LocalContext.current
    var token by remember { mutableStateOf("") }

    val userEmail by remember {
        mutableStateOf(viewModel.getStoredEmail(context))
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
            verticalArrangement = Arrangement.Center // Центрируем по вертикали
        ) {
            Text(
                text = stringResource(R.string.otp_verification),
                style = Typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.check_email_please),
                style = Typography.bodySmall,
                color = colorResource(R.color.SubTextDark)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.otp_code),
            style = Typography.labelMedium,
            modifier = Modifier.padding(bottom = 20.dp),
            color = colorResource(R.color.Text)
        )

        InputTextBox(
            modifier = Modifier,
            value = token,
            onValueChange = { token = it },
        )

        Spacer(modifier = Modifier.weight(0.2f))

        RegisterButton(
            modifier = Modifier,
            text = stringResource(R.string.send),
            onClick = {
//                viewModel.register(context, token) {
//
//                }
            },
            enabled = true
        )

        Spacer(modifier = Modifier.weight(1f))
    }

}
@Preview
@Composable
private fun VerificationScreenPreview() {
    VerificationScreen()
}