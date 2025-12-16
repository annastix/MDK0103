package com.example.shoesshop.data.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoesshop.R
import com.example.shoesshop.ui.theme.Typography

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(modifier = Modifier
        .background(colorResource(R.color.white))
        .fillMaxSize()
        .padding(horizontal = 20.dp))
    {
        Row(

        ){
            IconButton(
                onClick = { /* Действие для левой кнопки */ },
                modifier = Modifier.size(48.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.clock_1),
                    contentDescription = "Левая кнопка",
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Текст по центру
            Text(
                text = stringResource(R.string.main),
//                style = Typography.bodySmall,
                color = colorResource(R.color.Text),
//                fontWeight = FontWeight.Medium
            )

            // Правая кнопка-картинка
//            IconButton(
//                onClick = { /* Действие для правой кнопки */ },
//                modifier = Modifier.size(48.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.),
//                    contentDescription = "Правая кнопка",
//                    modifier = Modifier.fillMaxSize()
//                )
//            }
        }
    }
}
@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}