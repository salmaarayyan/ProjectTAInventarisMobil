package com.example.inventarisshowroom.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * ============================================================
 * SHOWROOM INVENTORY THEME
 * ============================================================
 *
 * Theme configuration untuk aplikasi Showroom Inventory
 * Menggunakan Material Design 3 dengan custom color scheme
 */

// Light Color Scheme (Default Mode)
private val LightColorScheme = lightColorScheme(
    // Primary colors - Teal Dark untuk branding
    primary = TealDark,                    // #385c6c - untuk AppBar, FAB, primary buttons
    onPrimary = TextOnPrimary,             // White text on primary
    primaryContainer = TealLight,          // Container variant
    onPrimaryContainer = TextPrimary,      // Text on primary container

    // Secondary colors - Optional, bisa dipakai untuk accent
    secondary = TealLight,
    onSecondary = TextOnPrimary,
    secondaryContainer = CreamDark,
    onSecondaryContainer = TextPrimary,

    // Background & Surface - Cream Warm
    background = CreamWarm,                // #fbe7cd - main background
    onBackground = TextPrimary,            // Black text on cream background
    surface = CreamLight,                  // Card surface color
    onSurface = TextPrimary,               // Text on surface
    surfaceVariant = Gray100,              // Alternative surface
    onSurfaceVariant = TextSecondary,      // Text on surface variant

    // Error colors - Red untuk delete dan error
    error = ActionDelete,                  // #F44336 - untuk tombol hapus
    onError = TextOnPrimary,               // White text on error
    errorContainer = Color(0xFFFFEBEE),    // Light red container
    onErrorContainer = ActionDeleteDark,   // Dark red text on error container

    // Outline & Borders
    outline = Gray400,                     // Border color
    outlineVariant = Gray300,              // Lighter border

    // Other
    scrim = Color.Black.copy(alpha = 0.5f) // Overlay for dialogs
)

// Dark Color Scheme (Optional - untuk future dark mode support)
private val DarkColorScheme = darkColorScheme(
    primary = TealLight,
    onPrimary = Gray900,
    primaryContainer = TealDark,
    onPrimaryContainer = Color.White,

    secondary = TealLight,
    onSecondary = Gray900,
    secondaryContainer = TealDarkVariant,
    onSecondaryContainer = Color.White,

    background = Gray900,
    onBackground = Color.White,
    surface = Gray800,
    onSurface = Color.White,
    surfaceVariant = Gray700,
    onSurfaceVariant = Gray300,

    error = ActionDelete,
    onError = Color.White,
    errorContainer = ActionDeleteDark,
    onErrorContainer = Color.White,

    outline = Gray600,
    outlineVariant = Gray700,

    scrim = Color.Black.copy(alpha = 0.7f)
)

/**
 * Main theme composable
 *
 * @param darkTheme Apakah menggunakan dark theme (default: false)
 * @param content Konten aplikasi yang akan dibungkus theme
 */
@Composable
fun InventarisShowroomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Pilih color scheme sesuai dark mode
    // NOTE: Saat ini kita force light theme karena design hanya untuk light mode
    val colorScheme = LightColorScheme // Ganti ke: if (darkTheme) DarkColorScheme else LightColorScheme kalau mau support dark mode

    // Update system bars color
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    // Apply Material Theme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}