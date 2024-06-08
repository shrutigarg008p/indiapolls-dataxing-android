package com.dataxing.indiapolls.ui.theme
import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.core.view.WindowCompat
import com.dataxing.indiapolls.R

private val lightScheme = lightColorScheme(
    primary = primary,
    onPrimary = primary,
    primaryContainer = primary,
    onPrimaryContainer = primary,
    secondary = primary,
    onSecondary = primary,
    secondaryContainer = primary,
    onSecondaryContainer = primary,
    tertiary = primary,
    onTertiary = primary,
    tertiaryContainer = primary,
    onTertiaryContainer = primary,
    error = errorLight,
    onError = errorLight,
    errorContainer = errorLight,
    onErrorContainer = errorLight,
    background = white,
    onBackground = white,
    surface = white,
    onSurface = white,
    surfaceVariant = white,
    onSurfaceVariant = white,
    outline = white,
    outlineVariant = white,
    scrim = white,
    inverseSurface = white,
    inverseOnSurface = white,
    inversePrimary = white
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AppTheme(
    content: @Composable() () -> Unit
) {
  val colorScheme = lightScheme
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = colorScheme.primary.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,
    content = content
  )
}
