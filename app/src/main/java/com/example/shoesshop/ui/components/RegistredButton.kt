package com.example.shoesshop.ui.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoesshop.R

@Composable
fun RegisterButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(13.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.Accent),
            contentColor =  colorResource(R.color.Background),
            disabledContainerColor = colorResource(R.color.Disable),
            disabledContentColor = colorResource(R.color.Background)
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            lineHeight = 16.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
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