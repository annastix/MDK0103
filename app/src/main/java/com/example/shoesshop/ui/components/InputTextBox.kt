package com.example.shoesshop.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
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
import com.example.shoesshop.ui.theme.AppTypography
import com.example.shoesshop.ui.theme.Typography

@Composable
fun InputTextBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = null, // Убираем label полностью
            placeholder = {
                if (placeholder.isNotEmpty()) {
                    Text(
                        text = placeholder,
                        style = AppTypography.bodyMedium14,
                        color = colorResource(R.color.Hint)
                    )
                }
            },
            isError = isError,
            supportingText = errorMessage?.let {
                {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        style = Typography.labelSmall
                    )
                }
            },
            enabled = enabled,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = colorResource(R.color.Background),
                focusedBorderColor = colorResource(R.color.Background),
                unfocusedContainerColor = colorResource(R.color.Background),
                focusedContainerColor = colorResource(R.color.Background),
                unfocusedPlaceholderColor = colorResource(R.color.Hint),
                focusedPlaceholderColor = colorResource(R.color.Hint)
            ),
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = colorResource(R.color.Text)
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            trailingIcon = trailingIcon,
            singleLine = singleLine
        )
    }
}

@Composable
fun PasswordInputTextBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Введите пароль",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    showPasswordToggle: Boolean = true // Добавляем опцию переключения
) {
    var passwordVisible by remember { mutableStateOf(false) }

    InputTextBox(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        isError = isError,
        errorMessage = errorMessage,
        enabled = enabled,
        visualTransformation = if (passwordVisible && showPasswordToggle) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = if (showPasswordToggle) {
            {
                val image = if (passwordVisible) {
                    // Используйте вашу иконку для скрытого пароля
                    painterResource(id = R.drawable.eye_open)
                } else {
                    // Используйте вашу иконку для видимого пароля
                    painterResource(id = R.drawable.eye_close)
                }

                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        painter = image,
                        contentDescription = if (passwordVisible) {
                            "Скрыть пароль"
                        } else {
                            "Показать пароль"
                        }
                    )
                }
            }
        } else null
    )
}