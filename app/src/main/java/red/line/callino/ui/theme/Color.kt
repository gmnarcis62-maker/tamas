package red.line.callino.ui.theme

import androidx.compose.ui.graphics.Color

// Frosted Glass Core Palette
val FrostedBg = Color(0xFFF3F4F9)             // Soft Frosted Background
val FrostedGlassWhite = Color(0xCCFFFFFF)     // 80% Frosted Glass Surface
val FrostedGlassSolid = Color(0xF5FFFFFF)     // 96% Frosted Solid Surface
val FrostedContainer = Color(0xFFFEF7FF)      // Light Purple-Tinted Container
val FrostedTintLight = Color(0xFFF3EDF7)      // Icon Pill Background
val FrostedTint = Color(0xFFE8DEF8)           // Active Tab & Badge Tint
val FrostedTintDeep = Color(0xFFEADDFF)       // Accent Pill Container
val FrostedBorder = Color(0xFFE2E8F0)         // Glass Border
val FrostedBorderLight = Color(0x99FFFFFF)    // Highlight Rim Border

// Brand & Text Colors
val FrostedPrimary = Color(0xFF6750A4)        // Royal Iris Violet
val FrostedSecondary = Color(0xFFB583FF)      // Violet Accent
val FrostedTertiary = Color(0xFFD0BCFF)       // Light Violet Indicator
val FrostedTextPrimary = Color(0xFF1C1B1F)    // Dark Text
val FrostedTextSecondary = Color(0xFF49454F)  // Slate Muted Text
val FrostedTextMuted = Color(0xFF79747E)      // Subtle Caption Text

// Brand Compatibility Identifiers
val CallinoPrimary = FrostedPrimary
val CallinoPrimaryDark = Color(0xFF4F378B)
val CallinoSecondary = Color(0xFFE63946)     // Reject/Vibrant Call Crimson
val CallinoTertiary = Color(0xFFF59E0B)      // Amber Gold
val CallinoSuccess = Color(0xFF10B981)       // Persian Emerald Green
val CallinoDanger = Color(0xFFEF4444)        // Reject Call Red

// Surfaces & Backgrounds
val DarkBg = FrostedBg
val DarkSurface = FrostedGlassSolid
val DarkSurfaceVariant = FrostedContainer
val DarkBorder = FrostedBorder
val DarkTextPrimary = FrostedTextPrimary
val DarkTextSecondary = FrostedTextSecondary

// Light Theme Surfaces & Backgrounds
val LightBg = FrostedBg
val LightSurface = FrostedGlassSolid
val LightSurfaceVariant = FrostedContainer
val LightBorder = FrostedBorder
val LightTextPrimary = FrostedTextPrimary
val LightTextSecondary = FrostedTextSecondary

// Accent Gradients
val FrostedGlassGradient = listOf(FrostedPrimary, FrostedSecondary)
val AuroraGradient = listOf(Color(0xFF6750A4), Color(0xFF9C27B0), Color(0xFFE91E63))
val CyberNeonGradient = listOf(Color(0xFF8A2387), Color(0xFFE94057), Color(0xFFF27121))
val PersianGoldGradient = listOf(Color(0xFFD4AF37), Color(0xFFB8860B), Color(0xFF8E0E00))
val DeepCosmosGradient = listOf(Color(0xFF1A103C), Color(0xFF3F2B96), Color(0xFF8E54E9))
val RomanticGradient = listOf(Color(0xFFFF758C), Color(0xFFFF7EB3), Color(0xFFFDA085))
