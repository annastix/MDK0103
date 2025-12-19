package com.example.shoesshop.ui.view

import android.util.Log
import android.widget.Toast
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
import com.example.shoesshop.R
import com.example.shoesshop.data.RetrofitInstance
import com.example.shoesshop.data.models.UpdatePasswordRequest
import com.example.shoesshop.ui.components.BackButton
import com.example.shoesshop.ui.components.PasswordInputTextBox
import com.example.shoesshop.ui.components.RegisterButton
import com.example.shoesshop.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun CreateNewPasswordScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onPasswordChanged: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var passwordOne by remember { mutableStateOf("") }
    var passwordTwo by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

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
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.set_new_passwd),
                style = Typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.set_new_passwd_to_account),
                style = Typography.bodySmall,
                modifier = Modifier.padding(bottom = 54.dp),
                color = colorResource(R.color.SubTextDark)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.password),
            style = Typography.labelMedium,
            modifier = Modifier.padding(bottom = 12.dp),
            color = colorResource(R.color.Text)
        )

        PasswordInputTextBox(
            modifier = Modifier,
            value = passwordOne,
            onValueChange = { passwordOne = it },
            placeholder = "........"
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.confirm_passwd),
            style = Typography.labelMedium,
            modifier = Modifier.padding(bottom = 12.dp),
            color = colorResource(R.color.Text)
        )

        PasswordInputTextBox(
            modifier = Modifier,
            value = passwordTwo,
            onValueChange = { passwordTwo = it },
            placeholder = "........"
        )

        Spacer(modifier = Modifier.weight(0.4f))

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterHorizontally),
                color = colorResource(R.color.Accent)
            )
        } else {
            RegisterButton(
                modifier = Modifier,
                text = stringResource(R.string.save_now),
                enabled = passwordOne.isNotEmpty() &&
                        passwordTwo.isNotEmpty() &&
                        passwordOne == passwordTwo,
                onClick = {
                    if (passwordOne != passwordTwo) {
                        Toast.makeText(
                            context,
                            "Пароли не совпадают",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@RegisterButton
                    }

                    scope.launch {
                        try {
                            isLoading = true
                            val resp = RetrofitInstance.userManagementService.updatePassword(
                                bearerToken = "Bearer",
                                body = UpdatePasswordRequest(passwordOne)
                            )
                            isLoading = false
                            if (resp.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Пароль успешно обновлён",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onPasswordChanged()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Ошибка: ${resp.code()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            isLoading = false
                            Log.e("CreateNewPasswordScreen", "update error", e)
                            Toast.makeText(
                                context,
                                "Ошибка сети: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun CreateNewPasswordScreenPreview() {
    CreateNewPasswordScreen()
}
