package com.example.shoesshop.data.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoesshop.R
import com.example.shoesshop.ui.components.BackButton
import com.example.shoesshop.ui.components.InputTextBox
import com.example.shoesshop.ui.components.RegisterButton
import com.example.shoesshop.ui.theme.Typography
import android.content.Context
import android.content.SharedPreferences

private const val PREF_NAME = "shoe_shop_prefs"
private const val KEY_RESET_EMAIL = "reset_email"

object PreferenceHelper {

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveResetEmail(context: Context, email: String) {
        prefs(context).edit().putString(KEY_RESET_EMAIL, email).apply()
    }

    fun getResetEmail(context: Context): String? =
        prefs(context).getString(KEY_RESET_EMAIL, null)

    fun clearResetEmail(context: Context) {
        prefs(context).edit().remove(KEY_RESET_EMAIL).apply()
    }
}

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEmailSaved: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

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
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.forgot),
                style = Typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(R.string.reset_password),
                style = Typography.bodySmall,
                modifier = Modifier
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(bottom = 40.dp),
                color = colorResource(R.color.SubTextDark)
            )
        }

        InputTextBox(
            modifier = Modifier.padding(bottom = 40.dp),
            value = email,
            onValueChange = { email = it },
            placeholder = "xyz@gmail.co"
        )

        RegisterButton(
            modifier = Modifier,
            text = stringResource(R.string.send),
            onClick = {
                if (email.isNotBlank()) {
                    PreferenceHelper.saveResetEmail(context, email.trim())
                    onEmailSaved()
                }
            }
        )

        Spacer(modifier = Modifier.weight(4f))
    }
}


@Preview
@Composable
private fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen()
}