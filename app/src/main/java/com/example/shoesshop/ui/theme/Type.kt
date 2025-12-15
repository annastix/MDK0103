package com.example.shoesshop.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    // Heading стили
    headlineLarge = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),

    // Body стили
    bodyLarge = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),

    // Subtitle и другие
    displayMedium = TextStyle( // Используем displayMedium для subtitle
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    )
)

// Дополнительные кастомные стили, если нужно
object AppTypography {
    // Heading стили
    val headingRegular34 = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp
    )

    val headingRegular32 = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp
    )

    val headingBold30 = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    )

    val headingRegular26 = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp
    )

    val headingRegular20 = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )

    val headingSemiBold16 = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    )

    // Body стили
    val bodyRegular24 = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    )

    val bodyRegular20 = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )

    val bodySemiBold18 = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    )

    val bodyMedium16 = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    )

    val bodyRegular16 = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )

    val bodyMedium14 = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )

    val bodyRegular14 = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )

    val bodyRegular12 = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )

    // Subtitle стили
    val subtitleRegular16 = TextStyle(
        fontFamily = Raleway,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
}