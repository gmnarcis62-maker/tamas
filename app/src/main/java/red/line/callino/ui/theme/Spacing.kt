package red.line.callino.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

// ============================================================
// 📏 CALINO DESIGN SYSTEM — Spacing / Radius / Elevation
// ============================================================

object Spacing {
    val xxs = 2.dp
    val xs  = 4.dp
    val sm  = 8.dp
    val md  = 12.dp
    val lg  = 16.dp
    val xl  = 20.dp
    val xxl = 24.dp
    val xxxl = 32.dp
    val huge = 48.dp

    // پدینگ افقی استاندارد صفحه
    val screenHorizontal = 20.dp

    // فاصله پایین صفحه (برای اینکه زیر BottomNav نره)
    val screenBottom = 96.dp
}

object Radius {
    val xs  = 8.dp
    val sm  = 12.dp
    val md  = 16.dp
    val lg  = 20.dp
    val xl  = 24.dp
    val xxl = 28.dp
    val full = 999.dp

    val cardShape = RoundedCornerShape(md)
    val buttonShape = RoundedCornerShape(sm)
    val chipShape = RoundedCornerShape(full)
    val sheetShape = RoundedCornerShape(topStart = xl, topEnd = xl)
    val bottomNavShape = RoundedCornerShape(xxl)
}

object Elevation {
    val none = 0.dp
    val sm   = 1.dp
    val md   = 2.dp
    val lg   = 4.dp
    val xl   = 8.dp
    val xxl  = 12.dp
}