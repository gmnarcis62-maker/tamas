package red.line.callino.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import red.line.callino.data.AssetNameHelper
import red.line.callino.data.AssetTheme
import red.line.callino.data.CallTheme
import red.line.callino.data.ThemeType
import red.line.callino.data.VipStatus
import red.line.callino.ui.components.CallThemeBackground
import red.line.callino.ui.components.CallinoChip
import red.line.callino.ui.components.VSpacer
import red.line.callino.ui.theme.Radius
import red.line.callino.ui.theme.Spacing

// ============================================================
// فقط ۴ دسته اصلی: همه، تصاویر، انیمیشن‌ها، ویدیوها
// ============================================================
private enum class ThemeFilter(val label: String, val icon: ImageVector) {
    ALL("همه", Icons.Default.Palette),
    IMAGES("تصاویر", Icons.Default.Image),
    ANIMATIONS("انیمیشن‌ها", Icons.Default.Animation),
    VIDEOS("ویدیوها", Icons.Default.Videocam);

    fun matches(theme: CallTheme): Boolean = when (this) {
        ALL -> true
        IMAGES -> theme.type == ThemeType.IMAGE
        ANIMATIONS -> theme.type == ThemeType.ANIMATION || theme.type == ThemeType.CUSTOM
        VIDEOS -> theme.type == ThemeType.VIDEO
    }
}

@Composable
fun ThemesScreen(
    themes: List<CallTheme>,
    assetThemes: List<AssetTheme> = emptyList(),
    activeThemeId: String,
    vipStatus: VipStatus = VipStatus(),
    onSelectTheme: (CallTheme) -> Unit,
    onPreviewTheme: (CallTheme) -> Unit,
    onOpenDetail: (CallTheme) -> Unit = {},
    onNavigateToVip: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf(ThemeFilter.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    var lockedThemeForDialog by remember { mutableStateOf<CallTheme?>(null) }

    // ادغام تم‌ها
    val allCombined = remember(themes, assetThemes) {
        val existingIds = themes.map { it.id }.toSet()
        val extras = assetThemes
            .filter { it.id !in existingIds }
            .map { it.toCallTheme() }
        themes + extras
    }

    // نام‌گذاری زیبا
    val namedThemes = remember(allCombined) {
        val counter = mutableMapOf<String, Int>()
        allCombined.map { theme ->
            val key = theme.type.name
            val idx = (counter[key] ?: 0) + 1
            counter[key] = idx
            val beautifulName = AssetNameHelper.beautifulTitle(theme, idx)
            theme.copy(titleFa = beautifulName)
        }
    }

    // فیلتر بر اساس دسته + جستجو
    val filteredThemes = remember(namedThemes, selectedFilter, searchQuery) {
        namedThemes.filter { theme ->
            if (!selectedFilter.matches(theme)) return@filter false
            if (searchQuery.isBlank()) return@filter true
            val q = searchQuery.trim().lowercase()
            theme.titleFa.lowercase().contains(q) ||
                    theme.category.lowercase().contains(q) ||
                    theme.descriptionFa.lowercase().contains(q)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.screenHorizontal),
        contentPadding = PaddingValues(top = Spacing.md, bottom = Spacing.xl),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg)
    ) {
        // ===================================================
        // Header
        // ===================================================
        item(span = { GridItemSpan(2) }) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "تم‌های تماس",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "پوسته‌های شیک برای شخصی‌سازی تماس‌های ورودی",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (!vipStatus.isVip) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Radius.full))
                                .clickable { onNavigateToVip() },
                            color = Color(0xFFFFD700).copy(alpha = 0.18f),
                            shape = RoundedCornerShape(Radius.full),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp, Color(0xFFFFD700)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "VIP",
                                    color = Color(0xFFB45309),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                VSpacer(14)

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("theme_search_input"),
                    placeholder = {
                        Text(
                            text = "جستجو در تم‌ها...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "پاک کردن",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(Radius.md),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        // ===================================================
        // Filters: همه / تصاویر / انیمیشن‌ها / ویدیوها
        // ===================================================
        item(span = { GridItemSpan(2) }) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                contentPadding = PaddingValues(vertical = Spacing.xs)
            ) {
                items(ThemeFilter.values().toList()) { filter ->
                    CallinoChip(
                        text = filter.label,
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter }
                    )
                }
            }
        }

        // ===================================================
        // Empty State
        // ===================================================
        if (filteredThemes.isEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.xxxl),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = if (searchQuery.isNotBlank()) Icons.Default.Search
                        else Icons.Default.Palette,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(56.dp)
                    )
                    VSpacer(12)
                    Text(
                        text = if (searchQuery.isNotBlank())
                            "تمی با این مشخصات پیدا نشد"
                        else
                            "هنوز تمی اضافه نشده",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    VSpacer(6)
                    Text(
                        text = "فایل‌های تصویر، ویدیو یا انیمیشن را در پوشه‌های assets قرار دهید تا خودکار نمایش داده شوند.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // ===================================================
        // Theme Cards
        // ===================================================
        itemsIndexed(filteredThemes, key = { _, t -> t.id }) { index, theme ->
            ThemeCardItem(
                theme = theme,
                isActive = theme.id == activeThemeId,
                isLocked = theme.isPremium && !vipStatus.isVip,
                onPreview = { onPreviewTheme(theme) },
                onSelect = { onSelectTheme(theme) },
                onOpenDetail = { onOpenDetail(theme) },
                onUnlockVip = { lockedThemeForDialog = theme }
            )
        }
    }

    // ===================================================
    // VIP Locked Dialog
    // ===================================================
    if (lockedThemeForDialog != null) {
        val theme = lockedThemeForDialog!!
        AlertDialog(
            onDismissRequest = { lockedThemeForDialog = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(Spacing.sm))
                    Text(
                        text = "پوسته ویژه VIP",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = theme.titleFa,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    VSpacer(8)
                    Text(
                        text = "با تهیه اشتراک تماسینو پلاس، به این پوسته و تمامی پوسته‌های ویژه دسترسی نامحدود خواهید داشت.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        lineHeight = 20.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        lockedThemeForDialog = null
                        onNavigateToVip()
                    },
                    shape = RoundedCornerShape(Radius.sm),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFB800),
                        contentColor = Color.Black
                    )
                ) {
                    Text("مشاهده اشتراک VIP", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { lockedThemeForDialog = null }) {
                    Text("بستن", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(Radius.lg)
        )
    }
}

// ===================================================
// Theme Card
// ===================================================
@Composable
private fun ThemeCardItem(
    theme: CallTheme,
    isActive: Boolean,
    isLocked: Boolean,
    onPreview: () -> Unit,
    onSelect: () -> Unit,
    onOpenDetail: () -> Unit,
    onUnlockVip: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(Radius.lg))
            .clickable { onOpenDetail() }
            .testTag("theme_card_${theme.id}"),
        shape = RoundedCornerShape(Radius.lg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            CallThemeBackground(
                theme = theme,
                dimAlpha = if (isLocked) 0.55f else 0.4f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.md),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when {
                        isActive -> StatusBadge(
                            text = "فعال",
                            bg = MaterialTheme.colorScheme.primary,
                            fg = Color.White,
                            icon = Icons.Default.CheckCircle
                        )
                        isLocked -> StatusBadge(
                            text = "VIP",
                            bg = Color(0xFFFFD700),
                            fg = Color.Black,
                            icon = Icons.Default.Lock
                        )
                        else -> StatusBadge(
                            text = theme.type.typeLabel(),
                            bg = Color.Black.copy(alpha = 0.55f),
                            fg = Color.White,
                            icon = theme.type.typeIcon()
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .clickable { onPreview() },
                        color = Color.Black.copy(alpha = 0.6f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "پیش‌نمایش",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Bottom
                Column {
                    Text(
                        text = theme.titleFa,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    VSpacer(8)

                    if (isLocked) {
                        Button(
                            onClick = onUnlockVip,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(34.dp),
                            shape = RoundedCornerShape(Radius.xs),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFB800),
                                contentColor = Color.Black
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "بازگشایی",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else if (!isActive) {
                        Button(
                            onClick = onSelect,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(34.dp),
                            shape = RoundedCornerShape(Radius.xs),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "انتخاب تم",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(34.dp)
                                .clip(RoundedCornerShape(Radius.xs))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(horizontal = Spacing.md),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✓ فعال",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(
    text: String,
    bg: Color,
    fg: Color,
    icon: ImageVector
) {
    Surface(
        shape = RoundedCornerShape(Radius.xs),
        color = bg
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = fg,
                modifier = Modifier.size(11.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = text,
                color = fg,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun ThemeType.typeLabel(): String = when (this) {
    ThemeType.VIDEO -> "ویدیو"
    ThemeType.ANIMATION -> "انیمیشن"
    ThemeType.IMAGE -> "تصویر"
    ThemeType.CUSTOM -> "شخصی"
}

private fun ThemeType.typeIcon(): ImageVector = when (this) {
    ThemeType.VIDEO -> Icons.Default.Videocam
    ThemeType.ANIMATION -> Icons.Default.Animation
    ThemeType.IMAGE -> Icons.Default.Image
    ThemeType.CUSTOM -> Icons.Default.Palette
}