package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MedicalPrimary,
    onPrimary = Color.White,
    primaryContainer = MedicalPrimaryDark,
    onPrimaryContainer = MedicalPrimaryLight,
    secondary = MedicalSecondary,
    onSecondary = Color.White,
    secondaryContainer = MedicalSecondary,
    onSecondaryContainer = MedicalSecondaryLight,
    tertiary = MedicalTertiary,
    background = MedicalBackgroundDark,
    surface = MedicalSurfaceDark,
    surfaceVariant = MedicalSurfaceVariantDark,
    onBackground = MedicalTextPrimaryDark,
    onSurface = MedicalTextPrimaryDark,
    onSurfaceVariant = MedicalTextSecondaryDark,
    error = StatusCritical,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = MedicalPrimary,
    onPrimary = Color.White,
    primaryContainer = MedicalPrimaryLight,
    onPrimaryContainer = MedicalPrimaryDark,
    secondary = MedicalSecondary,
    onSecondary = Color.White,
    secondaryContainer = MedicalSecondaryLight,
    onSecondaryContainer = MedicalSecondary,
    tertiary = MedicalTertiary,
    background = MedicalBackgroundLight,
    surface = MedicalSurfaceLight,
    surfaceVariant = MedicalSurfaceVariantLight,
    onBackground = MedicalTextPrimaryLight,
    onSurface = MedicalTextPrimaryLight,
    onSurfaceVariant = MedicalTextSecondaryLight,
    error = StatusCritical,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Healthcare branding consistent
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

