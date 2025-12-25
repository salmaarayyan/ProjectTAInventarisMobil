package com.example.inventarisshowroom.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * ============================================================
 * SHOWROOM INVENTORY COLOR PALETTE
 * ============================================================
 *
 * Primary Colors - untuk branding utama aplikasi
 * Background Colors - untuk latar belakang dan surface
 * Action Colors - untuk tombol dan interaksi
 * Semantic Colors - untuk status (error, success, warning)
 */

// ===== PRIMARY COLORS (Teal Dark - untuk AppBar, FAB, Primary Button) =====
val TealDark = Color(0xFF385c6c)           // Primary color - AppBar, FAB, buttons
val TealDarkVariant = Color(0xFF2a4754)    // Darker variant for pressed state
val TealLight = Color(0xFF4d7a8d)          // Lighter variant for hover

// ===== BACKGROUND COLORS (Cream Warm - untuk background utama) =====
val CreamWarm = Color(0xFFfbe7cd)          // Main background color
val CreamLight = Color(0xFFfef5eb)         // Lighter cream for cards
val CreamDark = Color(0xFFf5dab8)          // Darker cream for dividers

// ===== NEUTRAL COLORS (Abu-abu - untuk tombol batal, text secondary) =====
val Gray50 = Color(0xFFFAFAFA)             // Very light gray
val Gray100 = Color(0xFFF5F5F5)            // Light gray background
val Gray200 = Color(0xFFEEEEEE)            // Border gray
val Gray300 = Color(0xFFE0E0E0)            // Disabled gray
val Gray400 = Color(0xFFBDBDBD)            // Placeholder gray
val Gray500 = Color(0xFF9E9E9E)            // Secondary text gray
val Gray600 = Color(0xFF757575)            // Primary text gray (cancel button)
val Gray700 = Color(0xFF616161)            // Dark gray
val Gray800 = Color(0xFF424242)            // Darker gray
val Gray900 = Color(0xFF212121)            // Black text

// ===== ACTION COLORS =====
val ActionDelete = Color(0xFFF44336)       // Red - untuk tombol hapus
val ActionDeleteDark = Color(0xFFD32F2F)   // Darker red for pressed state
val ActionSuccess = Color(0xFF4CAF50)      // Green - untuk tombol tambah stok
val ActionWarning = Color(0xFFFF9800)      // Orange - untuk warning

// ===== TEXT COLORS =====
val TextPrimary = Gray900                  // Primary text color
val TextSecondary = Gray600                // Secondary text color
val TextTertiary = Gray500                 // Tertiary text color
val TextOnPrimary = Color.White            // Text on primary color (buttons, AppBar)

// ===== SEMANTIC COLORS (untuk status dan feedback) =====
val ErrorLight = Color(0xFFFFEBEE)         // Light error background
val SuccessLight = Color(0xFFE8F5E9)       // Light success background
val WarningLight = Color(0xFFFFF3E0)       // Light warning background

/**
 * ============================================================
 * USAGE NOTES:
 * ============================================================
 *
 * - Primary (TealDark) → AppBar, FAB, Primary Buttons, Links
 * - Background (CreamWarm) → Main app background
 * - Surface (CreamLight) → Cards, Dialogs
 * - Gray600 → Cancel buttons, secondary text
 * - ActionDelete (Red) → Delete buttons, error messages
 * - ActionSuccess (Green) → Success messages, add stock buttons
 */