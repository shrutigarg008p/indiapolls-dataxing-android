package com.dataxing.indiapolls.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.dataxing.indiapolls.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Poppins"),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Poppins"),
        fontProvider = provider,
    )
)

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily)
)

val poppinsFamily = FontFamily(
    androidx.compose.ui.text.font.Font(R.font.poppins, FontWeight.Normal),
    androidx.compose.ui.text.font.Font(R.font.poppins_medium, FontWeight.Medium),
    androidx.compose.ui.text.font.Font(R.font.poppins_bold, FontWeight.Bold)
)

val defaultHeaderTextView: TextStyle
    get() = baseline.headlineMedium.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Medium,
        color = textPrimary,
        fontSize = 24.sp,
        lineHeight = 30.sp,
    )

val defaultSubHeaderTextView: TextStyle
    get() = baseline.headlineSmall.copy(
        color = textPrimary,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal
    )

val defaultMediumTextView: TextStyle
    get() = baseline.headlineMedium.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Medium,
        color = textPrimary,
        lineHeight = 22.sp,
        fontSize = 16.sp
    )

val defaultRegularTextView: TextStyle
    get() = baseline.headlineSmall.copy(
        color = textPrimary,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal
    )

val smallSizeMediumFontTextView: TextStyle
    get() = baseline.headlineMedium.copy(
        color = textPrimary,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Medium,
    )
