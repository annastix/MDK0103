package com.example.shoesshop.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoesshop.R

@Composable
fun InputTextBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        label = {
            if (label.isNotEmpty()) {
                Text(
                    text = label,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                    color = colorResource(R.color.Text),
                )
            }
        },
        placeholder = {
            if (placeholder.isNotEmpty()) {
                Text(
                    text = placeholder,
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    color = colorResource(R.color.Background)
                )
            }
        },
        isError = isError,
        supportingText = errorMessage?.let {
            {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
        },
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = colorResource(R.color.Background),
            focusedBorderColor = colorResource(R.color.Background)
        ),
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    )
}

@Composable
fun PasswordInputTextBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Пароль",
    placeholder: String = "Введите пароль",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    InputTextBox(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        isError = isError,
        errorMessage = errorMessage,
        enabled = enabled
    )
}

@Composable
fun EmailInputTextBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Email",
    placeholder: String = "email@example.com",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    InputTextBox(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        isError = isError,
        errorMessage = errorMessage,
        enabled = enabled
    )
}

// Специализированный компонент для имени
@Composable
fun NameInputTextBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Ваше имя",
    placeholder: String = "Введите имя",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    InputTextBox(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        isError = isError,
        errorMessage = errorMessage,
        enabled = enabled
    )
}

@Preview
@Composable
private fun InputTextBoxPreview() {
    var text by remember { mutableStateOf("") }

    InputTextBox(
        value = text,
        onValueChange = { text = it },
        label = "Ваше имя",
        placeholder = "Введите ваше имя"
    )
}

@Preview
@Composable
private fun EmailInputTextBoxPreview() {
    var email by remember { mutableStateOf("") }

    EmailInputTextBox(
        value = email,
        onValueChange = { email = it },
        label = "Email",
        placeholder = "email@example.com"
    )
}

@Preview
@Composable
private fun PasswordInputTextBoxPreview() {
    var password by remember { mutableStateOf("") }

    PasswordInputTextBox(
        value = password,
        onValueChange = { password = it },
        label = "Пароль",
        placeholder = "Введите пароль"
    )
}
