package red.line.callino.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import red.line.callino.data.AssetTheme
import red.line.callino.data.CallTheme
import red.line.callino.data.ThemeType
import red.line.callino.data.VipStatus
import red.line.callino.ui.components.CallThemeBackground
import red.line.callino.ui.theme.FrostedBorder
import red.line.callino.ui.theme.FrostedGlassSolid
import red.line.callino.ui.theme.FrostedPrimary
import red.line.callino.ui.theme.FrostedSecondary
import red.line.callino.ui.theme.FrostedTextPrimary
import red.line.callino.ui.theme.FrostedTextSecondary
import red.line.callino.ui.theme.FrostedTint
import red.line.callino.ui.theme.FrostedTintDeep
import red.line.callino.ui.theme.FrostedTintLight

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
    var selectedCategory by remember { mutableStateOf("همه") }
    var searchQuery by remember { mutableStateOf("") }
    var lockedThemeForDialog by remember { mutableStateOf<CallTheme?>(null) }
    
    // Store Categories as requested
    val storeCategories = listOf(
        "همه",
        "طبیعت 🌿",
        "نئون 🌌",
        "عاشقانه ❤️",
        "لوکس 💎",
        "فضایی 🚀",
        "مینیمال",
        "مناسبتی",
        "ویدئو",
        "انیمیشن",
        "تصاویر"
    )

    // Merge asset themes if not already in themes list
    val allCombinedThemes = remember(themes, assetThemes) {
        val existingIds = themes.map { it.id }.toSet()
        val extraAssetThemes = assetThemes.filter { it.id !in existingIds }.map { it.toCallTheme() }
        themes + extraAssetThemes
    }

    val filteredThemes = remember(allCombinedThemes, selectedCategory, searchQuery) {
        allCombinedThemes.filter { theme ->
            // Category filter
            val matchesCategory = when (selectedCategory) {
                "همه" -> true
                "ویدئو" -> theme.type == ThemeType.VIDEO
                "انیمیشن" -> theme.type == ThemeType.ANIMATION
                "تصاویر" -> theme.type == ThemeType.IMAGE
                else -> {
                    theme.category.contains(selectedCategory.replace(Regex("[^\\p{L}\\p{Nd}]"), "").trim()) ||
                    theme.category == selectedCategory
                }
            }

            // Search query filter
            val matchesSearch = if (searchQuery.isBlank()) {
                true
            } else {
                val q = searchQuery.trim().lowercase()
                theme.titleFa.lowercase().contains(q) ||
                theme.titleEn.lowercase().contains(q) ||
                theme.category.lowercase().contains(q) ||
                theme.descriptionFa.lowercase().contains(q)
            }

            matchesCategory && matchesSearch
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item(span = { GridItemSpan(2) }) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = null,
                                tint = FrostedPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "فروشگاه و کاتالوگ تم‌های تماس",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = FrostedTextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "مجموعه پوسته‌های شیک و متحرک برای شخصی‌سازی تماس‌های ورودی",
                            style = MaterialTheme.typography.bodySmall,
                            color = FrostedTextSecondary
                        )
                    }

                    if (!vipStatus.isVip) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onNavigateToVip() }
                                .testTag("themes_vip_upgrade_badge"),
                            color = Color(0xFFFFD700).copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD700)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "ارتقا به VIP",
                                    color = Color(0xFFFFD700),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("theme_search_input"),
                    placeholder = {
                        Text(
                            text = "جستجوی تم (نئون، طبیعت، لوکس، کهکشان...)",
                            color = FrostedTextSecondary,
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "جستجو",
                            tint = FrostedTextSecondary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "پاک کردن",
                                    tint = FrostedTextSecondary
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FrostedPrimary,
                        unfocusedBorderColor = FrostedBorder,
                        focusedContainerColor = FrostedGlassSolid,
                        unfocusedContainerColor = FrostedGlassSolid,
                        focusedTextColor = FrostedTextPrimary,
                        unfocusedTextColor = FrostedTextPrimary
                    )
                )
            }
        }

        // Category Filter Chips
        item(span = { GridItemSpan(2) }) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(storeCategories) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { selectedCategory = category }
                            .testTag("filter_chip_$category"),
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) FrostedPrimary else FrostedTintLight,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) FrostedPrimary else FrostedBorder
                        )
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else FrostedTextPrimary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Empty state when catalog has no themes, or search yields no results
        if (themes.isEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                        .testTag("empty_themes_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = FrostedTintLight,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = null,
                                    tint = FrostedPrimary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "هنوز تمی اضافه نشده است",
                            fontWeight = FontWeight.Bold,
                            color = FrostedTextPrimary,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "فایل‌های تم دلخواه (تصویر، ویدیو یا انیمیشن) را در پوشه‌های assets قرار دهید تا به صورت خودکار در این کاتالوگ نمایش داده شوند.",
                            textAlign = TextAlign.Center,
                            color = FrostedTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        } else if (filteredThemes.isEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = FrostedTextSecondary,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "تمی با این مشخصات یافت نشد",
                            fontWeight = FontWeight.Bold,
                            color = FrostedTextPrimary,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "می‌توانید عبارت جستجو را تغییر دهید یا دسته دیگری را انتخاب نمایید.",
                            textAlign = TextAlign.Center,
                            color = FrostedTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Themes List Grid
        items(filteredThemes) { theme ->
            val isActive = theme.id == activeThemeId
            val isLocked = theme.isPremium && !vipStatus.isVip

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(245.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .clickable { onOpenDetail(theme) }
                    .testTag("theme_card_${theme.id}"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
                border = androidx.compose.foundation.BorderStroke(
                    width = if (isActive) 2.dp else 1.dp,
                    color = if (isActive) FrostedPrimary else if (isLocked) Color(0xFFFFD700).copy(alpha = 0.5f) else FrostedBorder
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Visual Preview
                    CallThemeBackground(
                        theme = theme,
                        dimAlpha = if (isLocked) 0.55f else 0.35f
                    )

                    // Overlay information and button
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isActive) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = FrostedPrimary
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "فعال",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            } else if (isLocked) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFFFFD700).copy(alpha = 0.9f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = "قفل VIP",
                                            tint = Color.Black,
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "VIP",
                                            color = Color.Black,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color.Black.copy(alpha = 0.55f)
                                ) {
                                    Text(
                                        text = theme.category,
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            // Quick Preview / Simulation Eye Icon
                            Surface(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .clickable { onPreviewTheme(theme) },
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

                        // Bottom Title, Type Tag & Select / Unlock Button
                        Column {
                            // Type Tag
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 2.dp)
                            ) {
                                val typeIcon = when (theme.type) {
                                    ThemeType.VIDEO -> Icons.Default.Videocam
                                    ThemeType.ANIMATION -> Icons.Default.Animation
                                    else -> Icons.Default.Image
                                }
                                val typeName = when (theme.type) {
                                    ThemeType.VIDEO -> "ویدیو"
                                    ThemeType.ANIMATION -> "انیمیشن"
                                    else -> "تصویر"
                                }
                                Icon(
                                    imageVector = typeIcon,
                                    contentDescription = null,
                                    tint = FrostedSecondary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = typeName,
                                    color = FrostedSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Text(
                                text = theme.titleFa,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            if (isLocked) {
                                Button(
                                    onClick = { lockedThemeForDialog = theme },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(36.dp)
                                        .testTag("unlock_vip_theme_btn_${theme.id}"),
                                    shape = RoundedCornerShape(18.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFFB800),
                                        contentColor = Color.Black
                                    ),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.WorkspacePremium,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "بازگشایی VIP",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            } else {
                                Button(
                                    onClick = { onSelectTheme(theme) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(36.dp)
                                        .testTag("apply_theme_btn_${theme.id}"),
                                    shape = RoundedCornerShape(18.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isActive) FrostedTintDeep else FrostedPrimary,
                                        contentColor = if (isActive) FrostedPrimary else Color.White
                                    ),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(
                                        text = if (isActive) "✓ فعال" else "انتخاب تم",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Locked VIP Theme Explanation Dialog
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "پوسته ویژه VIP: ${theme.titleFa}",
                        color = FrostedTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "این پوسته از مجموعه پوسته‌های ویژه و متحرک تماسینو است.",
                        color = FrostedTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "با تهیه اشتراک تماسینو پلاس، به این پوسته و تمامی پوسته‌های نئونی، کیهانی و ویدیویی دسترسی نامحدود خواهید داشت.",
                        color = FrostedTextSecondary,
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
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFB800),
                        contentColor = Color.Black
                    )
                ) {
                    Text("مشاهده و خرید اشتراک VIP", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { lockedThemeForDialog = null }) {
                    Text("بستن", color = FrostedTextSecondary)
                }
            },
            containerColor = FrostedGlassSolid,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

