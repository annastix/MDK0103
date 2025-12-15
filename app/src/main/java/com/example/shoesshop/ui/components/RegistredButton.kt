package com.example.shoesshop.ui.components

import android.graphics.Paint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoesshop.R
import com.example.shoesshop.ui.theme.Raleway
import com.example.shoesshop.ui.theme.Typography

@Composable
fun RegisterButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = { if (enabled) onClick() }, // Проверяем enabled внутри onClick
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(13.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) {
                colorResource(R.color.Accent)
            } else {
                colorResource(R.color.Disable)
            },
            disabledContainerColor = colorResource(R.color.Disable), // Явно указываем цвет для disabled состояния
            disabledContentColor = colorResource(R.color.Background) // Цвет текста для disabled состояния
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = Typography.displayMedium,
            color = if (enabled) {
                colorResource(R.color.Background)
            } else {
                colorResource(R.color.Background) // или другой цвет для неактивного текста
            }
        )
    }
}


@Preview
@Composable
private fun RegisterButtonPreview() {
    RegisterButton(
        modifier = Modifier,
        text = "Зарегистрироваться",
        onClick = {}
    )
}