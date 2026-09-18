package red.line.callino.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================
// 🎨 CALINO DESIGN SYSTEM — Color Palette
// Material You + Premium Dark hybrid
// ============================================================

// --- Primary (Indigo) ---
val Indigo50  = Color(0xFFEEF2FF)
val Indigo100 = Color(0xFFE0E7FF)
val Indigo200 = Color(0xFFC7D2FE)
val Indigo300 = Color(0xFFA5B4FC)
val Indigo400 = Color(0xFF818CF8)
val Indigo500 = Color(0xFF6366F1)   // ← primary
val Indigo600 = Color(0xFF4F46E5)
val Indigo700 = Color(0xFF4338CA)
val Indigo800 = Color(0xFF3730A3)
val Indigo900 = Color(0xFF312E81)

// --- Secondary (Violet) ---
val Violet50  = Color(0xFFF5F3FF)
val Violet100 = Color(0xFFEDE9FE)
val Violet200 = Color(0xFFDDD6FE)
val Violet300 = Color(0xFFC4B5FD)
val Violet400 = Color(0xFFA78BFA)
val Violet500 = Color(0xFF8B5CF6)   // ← secondary
val Violet600 = Color(0xFF7C3AED)
val Violet700 = Color(0xFF6D28D9)
val Violet800 = Color(0xFF5B21B6)
val Violet900 = Color(0xFF4C1D95)

// --- Accent / VIP (Amber/Gold) ---
val Gold50  = Color(0xFFFFFBEB)
val Gold100 = Color(0xFFFEF3C7)
val Gold300 = Color(0xFFFCD34D)
val Gold400 = Color(0xFFFBBF24)
val Gold500 = Color(0xFFF59E0B)     // ← accent
val Gold600 = Color(0xFFD97706)

// --- Success ---
val Success500 = Color(0xFF10B981)
val Success300 = Color(0xFF6EE7B7)
val Success100 = Color(0xFFD1FAE5)

// --- Error ---
val Error500 = Color(0xFFEF4444)
val Error400 = Color(0xFFF87171)
val Error100 = Color(0xFFFEE2E2)

// --- Neutrals (Light) ---
val Neutral0   = Color(0xFFFFFFFF)
val Neutral50  = Color(0xFFFAFAFA)
val Neutral100 = Color(0xFFF4F4F5)
val Neutral200 = Color(0xFFE4E4E7)
val Neutral300 = Color(0xFFD4D4D8)
val Neutral400 = Color(0xFFA1A1AA)
val Neutral500 = Color(0xFF71717A)
val Neutral600 = Color(0xFF52525B)
val Neutral700 = Color(0xFF3F3F46)
val Neutral800 = Color(0xFF27272A)
val Neutral900 = Color(0xFF18181B)

// --- Neutrals (Dark — Navy-based, warm) ---
val Ink50  = Color(0xFFF8FAFC)
val Ink100 = Color(0xFFF1F5F9)
val Ink200 = Color(0xFFE2E8F0)
val Ink300 = Color(0xFFCBD5E1)
val Ink400 = Color(0xFF94A3B8)
val Ink500 = Color(0xFF64748B)
val Ink600 = Color(0xFF475569)
val Ink700 = Color(0xFF334155)
val Ink800 = Color(0xFF1E293B)
val Ink900 = Color(0xFF0F172A)
val Ink950 = Color(0xFF020617)

// ============================================================
// 🎯 SEMANTIC TOKENS
// ============================================================

// -------- Light Mode --------
object LightPalette {
    val background       = Neutral50
    val surface          = Neutral0
    val surfaceVariant   = Neutral100
    val surfaceElevated  = Neutral0
    val outline          = Neutral200
    val outlineStrong    = Neutral300

    val primary          = Indigo500
    val onPrimary        = Neutral0
    val primaryContainer = Indigo50
    val onPrimaryContainer = Indigo900

    val secondary        = Violet500
    val onSecondary      = Neutral0
    val secondaryContainer = Violet50

    val accent           = Gold500
    val onAccent         = Neutral900
    val accentContainer  = Gold50

    val success          = Success500
    val error            = Error500
    val errorContainer   = Error100

    val textPrimary      = Neutral900
    val textSecondary    = Neutral600
    val textTertiary     = Neutral400
    val textOnDark       = Neutral0
}

// -------- Dark Mode --------
object DarkPalette {
    val background       = Ink900
    val surface          = Ink800
    val surfaceVariant   = Ink700
    val surfaceElevated  = Color(0xFF243349)
    val outline          = Color(0xFF2D3F5A)
    val outlineStrong    = Color(0xFF3D5273)

    val primary          = Indigo400
    val onPrimary        = Ink950
    val primaryContainer = Color(0xFF2E3A6B)
    val onPrimaryContainer = Indigo100

    val secondary        = Violet400
    val onSecondary      = Ink950
    val secondaryContainer = Color(0xFF3D2E6B)

    val accent           = Gold400
    val onAccent         = Ink950
    val accentContainer  = Color(0xFF4A3A1E)

    val success          = Success300
    val error            = Error400
    val errorContainer   = Color(0xFF4A1F1F)

    val textPrimary      = Ink50
    val textSecondary    = Ink400
    val textTertiary     = Ink500
    val textOnDark       = Ink950
}