package com.example.shoesshop.data.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoesshop.R
import com.example.shoesshop.ui.theme.Typography

@Composable
fun RegisterAccount(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = stringResource(R.string.register),
            style = Typography.labelSmall
        )
    }
}

@Preview
@Composable
private fun RegisterAccountPreview() {
    
}