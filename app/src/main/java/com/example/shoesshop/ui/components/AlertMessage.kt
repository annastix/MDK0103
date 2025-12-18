package com.example.shoesshop.ui.components

import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoesshop.R
import com.example.shoesshop.ui.theme.Typography


@Composable
fun AlertMessage(
    modifier: Modifier = Modifier,
    title: String = "",
    description: String = "",
    onCancelClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    showCancelButton: Boolean = true
) {
    AlertDialog(
        modifier = Modifier,
        onDismissRequest = {
            // Закрытие по клику вне диалога
            onCancelClick()
        },
        containerColor = colorResource(R.color.white),
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (showCancelButton) {
                    TextButton(
                        onClick = {
                            onCancelClick() // ← Закрытие при нажатии "Отмена"
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = colorResource(R.color.Text),
                            containerColor = colorResource(R.color.SubTextLight)
                        ),
                        modifier = modifier.weight(0.5f)
                    ) {
                        Text(
                            stringResource(R.string.cancel),
                            style = Typography.displayMedium
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }

                TextButton(
                    onClick = {
                        onConfirmClick() // ← Закрытие при нажатии "OK"
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colorResource(R.color.white),
                        containerColor = colorResource(R.color.Accent)
                    ),
                    modifier = modifier.weight(0.5f)
                ) {
                    Text(
                        stringResource(R.string.ok),
                        style = Typography.displayMedium
                    )
                }
            }
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally),
                text = title,
                fontFamily = Typography.headlineSmall.fontFamily,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally),
                text = description,
                fontStyle = Typography.bodySmall.fontStyle,
                color = colorResource(R.color.SubTextDark)
            )
        }
    )
}

@Preview
@Composable
private fun AlertMessagePreview() {
    AlertMessage(modifier = Modifier,"Title", "{Description}")
}