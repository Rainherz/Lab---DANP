package com.example.lab05.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AppThemeMode {
    BLUE,
    GREEN,
    PURPLE,
    CYBERPUNK
}

@Composable
fun ModularStoreTheme(
    themeMode: AppThemeMode,
    isDarkMode: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        AppThemeMode.BLUE -> {
            if (isDarkMode) {
                darkColorScheme(
                    primary = BluePrimaryDark,
                    secondary = Color(0xFF42A5F5),
                    background = Color(0xFF0F172A),
                    surface = Color(0xFF1E293B),
                    onBackground = Color(0xFFF8FAFC),
                    onSurface = Color(0xFFE2E8F0)
                )
            } else {
                lightColorScheme(
                    primary = BluePrimary,
                    secondary = Color(0xFF1E88E5),
                    background = Color(0xFFF8FAFC),
                    surface = Color.White,
                    onBackground = Color(0xFF0F172A),
                    onSurface = Color(0xFF334155)
                )
            }
        }
        AppThemeMode.GREEN -> {
            if (isDarkMode) {
                darkColorScheme(
                    primary = GreenPrimaryDark,
                    secondary = Color(0xFF66BB6A),
                    background = Color(0xFF0A1C10),
                    surface = Color(0xFF142F1C),
                    onBackground = Color(0xFFECFDF5),
                    onSurface = Color(0xFFD1FAE5)
                )
            } else {
                lightColorScheme(
                    primary = GreenPrimary,
                    secondary = Color(0xFF43A047),
                    background = Color(0xFFF0FDF4),
                    surface = Color.White,
                    onBackground = Color(0xFF064E3B),
                    onSurface = Color(0xFF0F766E)
                )
            }
        }
        AppThemeMode.PURPLE -> {
            if (isDarkMode) {
                darkColorScheme(
                    primary = PurplePrimaryDark,
                    secondary = Color(0xFFAB47BC),
                    background = Color(0xFF170C2E),
                    surface = Color(0xFF2D1B4E),
                    onBackground = Color(0xFFFDF4FF),
                    onSurface = Color(0xFFF5D0FE)
                )
            } else {
                lightColorScheme(
                    primary = PurplePrimary,
                    secondary = Color(0xFF8E24AA),
                    background = Color(0xFFFAF5FF),
                    surface = Color.White,
                    onBackground = Color(0xFF4A044E),
                    onSurface = Color(0xFF701A75)
                )
            }
        }
        AppThemeMode.CYBERPUNK -> {
            if (isDarkMode) {
                darkColorScheme(
                    primary = CyberpunkPrimaryDark,
                    secondary = CyberpunkSecondaryDark,
                    background = CyberpunkBackgroundDark,
                    surface = CyberpunkSurfaceDark,
                    onPrimary = CyberpunkOnPrimaryDark,
                    onBackground = CyberpunkOnBackgroundDark,
                    onSurface = CyberpunkOnSurfaceDark
                )
            } else {
                lightColorScheme(
                    primary = CyberpunkPrimaryLight,
                    secondary = CyberpunkSecondaryLight,
                    background = CyberpunkBackgroundLight,
                    surface = CyberpunkSurfaceLight,
                    onPrimary = CyberpunkOnPrimaryLight,
                    onBackground = CyberpunkOnBackgroundLight,
                    onSurface = CyberpunkOnSurfaceLight
                )
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}