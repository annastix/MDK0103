package com.example.shoesshop.data.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoeshop.ui.components.BackButton
import com.example.shoesshop.R
import com.example.shoesshop.ui.components.InputTextBox
import com.example.shoesshop.ui.components.PasswordInputTextBox
import com.example.shoesshop.ui.components.RegisterButton
import com.example.shoesshop.ui.theme.Typography

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    resetPassword: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(colorResource(R.color.white))
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.weight(0.5f))
        BackButton(
            onClick = onBackClick
        )
        Spacer(modifier = Modifier.height(21.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.hello_again),
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

        Spacer(modifier = Modifier.height(30.dp))

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = resetPassword
            ) {
                Text(
                    modifier = Modifier,
                    text = stringResource(R.string.recovery),
                    color = colorResource(R.color.SubTextDark),
                    style = Typography.displaySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        RegisterButton(
            modifier = Modifier,
            text = stringResource(R.string.sign_in),
            onClick = {}
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 47.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            TextButton(
                onClick = onRegisterClick,
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
                            append(stringResource(id = R.string.new_user))
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = colorResource(R.color.Text),
                                fontFamily = Typography.bodySmall.fontFamily,
                                fontSize = Typography.bodySmall.fontSize,
                            )
                        ) {
                            append(stringResource(id = R.string.create_account))
                        }
                    }
                )
            }
        }
    }
}
@Preview
@Composable
private fun SignInScreenPreview() {
    SignInScreen()
}