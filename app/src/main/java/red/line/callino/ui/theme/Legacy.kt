package red.line.callino.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================
// 🔄 LEGACY BRIDGE
// این فایل موقتیه و نام‌های قدیمی سیستم طراحی (Frosted* ، Callino*
// و گرادیانت‌ها) رو به پالت جدید LightPalette/DarkPalette وصل می‌کنه.
//
// ⚠️ این فایل در فاز ۳ (بازطراحی صفحه‌ها) حذف می‌شه.
// ============================================================

// --- پس‌زمینه‌ها ---
val FrostedBg: Color = LightPalette.background
val FrostedGlassSolid: Color = LightPalette.surface
val FrostedGlassWhite: Color = LightPalette.surfaceElevated
val FrostedContainer: Color = LightPalette.surfaceVariant

// --- رنگ‌های اصلی ---
val FrostedPrimary: Color = LightPalette.primary
val FrostedSecondary: Color = LightPalette.secondary
val CallinoPrimary: Color = LightPalette.primary
val CallinoSecondary: Color = LightPalette.secondary
val CallinoSuccess: Color = LightPalette.success
val CallinoDanger: Color = LightPalette.error

// --- رنگ‌های متن ---
val FrostedTextPrimary: Color = LightPalette.textPrimary
val FrostedTextSecondary: Color = LightPalette.textSecondary
val FrostedTextMuted: Color = LightPalette.textTertiary

// --- حاشیه ---
val FrostedBorder: Color = LightPalette.outline

// --- تینت‌ها ---
val FrostedTint: Color = LightPalette.primaryContainer
val FrostedTintLight: Color = LightPalette.primaryContainer.copy(alpha = 0.5f)
val FrostedTintDeep: Color = LightPalette.primaryContainer

// ============================================================
// 🎨 گرادیانت‌های تم (List<Color> — نه Brush)
// این‌ها در Components.kt و GradientThemeRenderer.kt به صورت
// لیست رنگ استفاده می‌شن (با [0]، [1] و getOrNull).
// ============================================================

val AuroraGradient: List<Color> = listOf(
    Color(0xFF06B6D4),   // Cyan
    Color(0xFF10B981),   // Emerald
    Color(0xFF8B5CF6)    // Violet
)

val CyberNeonGradient: List<Color> = listOf(
    Color(0xFF06B6D4),   // Cyan
    Color(0xFF6366F1),   // Indigo
    Color(0xFFEC4899)    // Pink
)

val DeepCosmosGradient: List<Color> = listOf(
    Color(0xFF1E1B4B),   // Deep Indigo
    Color(0xFF312E81),   // Indigo
    Color(0xFF6D28D9)    // Violet
)

val PersianGoldGradient: List<Color> = listOf(
    Color(0xFFF59E0B),   // Gold
    Color(0xFFD97706),   // Amber
    Color(0xFFB45309)    // Dark Amber
)

val RomanticGradient: List<Color> = listOf(
    Color(0xFFEC4899),   // Pink
    Color(0xFFDB2777),   // Rose
    Color(0xFFBE185D)    // Deep Rose
)