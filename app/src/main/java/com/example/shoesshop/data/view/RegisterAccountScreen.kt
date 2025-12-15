package com.example.shoesshop.data.view

import android.widget.ImageButton
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoeshop.ui.components.BackButton
import com.example.shoesshop.R
import com.example.shoesshop.data.viewModel.RegisterAccountViewModel
import com.example.shoesshop.ui.components.InputTextBox
import com.example.shoesshop.ui.components.RegisterButton
import com.example.shoesshop.ui.theme.Typography

@Composable
fun RegisterAccount(
    onNavigateToSignIn: () -> Unit,
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    viewModel: RegisterAccountViewModel
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(colorResource(R.color.white))
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        BackButton(
            onClick = onBackClick
        )
        Column(
            modifier = Modifier
            .fillMaxWidth(),
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

        InputTextBox(
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
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(MaterialTheme.shapes.small)
                    .selectable(
                        selected = showPassword,
                        onClick = { showPassword = !showPassword },
                        role = Role.Checkbox
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(MaterialTheme.shapes.small)
                        .border(
                            width = 2.dp,
                            color = if (showPassword) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            shape = MaterialTheme.shapes.small
                        )
                        .background(
                            if (showPassword) colorResource(R.color.Accent) else Color.Transparent
                        )
                )

                if (showPassword) {
                    Icon(
                        painter = painterResource(id = R.drawable.policy_check),
                        contentDescription = "Выбрано",
                        modifier = Modifier.size(16.dp),
                        tint = colorResource(R.color.black)
                    )
                } else {
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

        RegisterButton(
            modifier = Modifier,
            text = stringResource(R.string.sign_up),
            onClick = {}
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(
                onClick = onLoginClick,
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
                            append(stringResource(id = R.string.sign_in))
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