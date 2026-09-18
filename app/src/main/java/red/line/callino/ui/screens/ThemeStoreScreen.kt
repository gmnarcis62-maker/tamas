package red.line.callino.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import red.line.callino.AssetImage
import red.line.callino.data.CallTheme
import red.line.callino.data.ThemePackage
import red.line.callino.data.ThemeStoreRepository
import red.line.callino.data.VipStatus
import red.line.callino.ui.theme.FrostedBg
import red.line.callino.ui.theme.FrostedBorder
import red.line.callino.ui.theme.FrostedContainer
import red.line.callino.ui.theme.FrostedGlassSolid
import red.line.callino.ui.theme.FrostedGlassWhite
import red.line.callino.ui.theme.FrostedPrimary
import red.line.callino.ui.theme.FrostedSecondary
import red.line.callino.ui.theme.FrostedTextMuted
import red.line.callino.ui.theme.FrostedTextPrimary
import red.line.callino.ui.theme.FrostedTextSecondary
import red.line.callino.ui.theme.FrostedTint
import red.line.callino.ui.theme.FrostedTintDeep

@Composable
fun ThemeStoreScreen(
    packages: List<ThemePackage>,
    allThemes: List<CallTheme>,
    downloadProgressMap: Map<String, Float>,
    vipStatus: VipStatus,
    onOpenPackageDetail: (ThemePackage) -> Unit,
    onPreviewTheme: (CallTheme) -> Unit,
    onDownloadPackage: (ThemePackage) -> Unit,
    onNavigateToVip: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("همه") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredPackages = remember(packages, selectedCategory, searchQuery) {
        packages.filter { pkg ->
            val matchesCategory = selectedCategory == "همه" || pkg.category.contains(selectedCategory.replace(Regex("[^\\p{L}\\s]"), "").trim())
            val matchesSearch = searchQuery.isBlank() ||
                    pkg.titleFa.contains(searchQuery, ignoreCase = true) ||
                    pkg.description.contains(searchQuery, ignoreCase = true) ||
                    pkg.tags.any { it.contains(searchQuery, ignoreCase = true) }
            matchesCategory && matchesSearch
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FrostedBg)
        ) {
            // Store Top Bar
            StoreHeader(
                vipStatus = vipStatus,
                totalPacksCount = packages.size,
                onOpenVip = onNavigateToVip
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 90.dp)
            ) {
                // VIP Hero Promotion Card
                if (!vipStatus.isVip) {
                    item {
                        StoreVipPromoBanner(onUpgradeClick = onNavigateToVip)
                    }
                }

                // Search Box
                item {
                    StoreSearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it }
                    )
                }

                // Category Filter Chips
                item {
                    StoreCategoryRow(
                        categories = ThemeStoreRepository.STORE_CATEGORIES,
                        selectedCategory = selectedCategory,
                        onSelectCategory = { selectedCategory = it }
                    )
                }

                // Featured / Package Cards
                if (filteredPackages.isEmpty()) {
                    item {
                        EmptyStoreState()
                    }
                } else {
                    items(filteredPackages, key = { it.id }) { pkg ->
                        val downloadProgress = downloadProgressMap[pkg.id]
                        val firstTheme = allThemes.find { it.id in pkg.themeIds } ?: allThemes.firstOrNull()

                        ThemePackageCard(
                            themePackage = pkg,
                            isUserVip = vipStatus.isVip,
                            downloadProgress = downloadProgress,
                            onCardClick = { onOpenPackageDetail(pkg) },
                            onPreviewClick = {
                                if (firstTheme != null) {
                                    onPreviewTheme(firstTheme)
                                } else {
                                    onOpenPackageDetail(pkg)
                                }
                            },
                            onDownloadClick = { onDownloadPackage(pkg) },
                            onVipClick = onNavigateToVip
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StoreHeader(
    vipStatus: VipStatus,
    totalPacksCount: Int,
    onOpenVip: () -> Unit
) {
    Surface(
        color = FrostedGlassSolid,
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        border = BorderStroke(1.dp, FrostedBorder),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(FrostedPrimary, FrostedSecondary)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = "فروشگاه تم",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = "فروشگاه پوسته‌های تماسینو",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = FrostedTextPrimary
                    )
                    Text(
                        text = "$totalPacksCount پک اختصاصی آماده دریافت",
                        fontSize = 12.sp,
                        color = FrostedTextSecondary
                    )
                }
            }

            // VIP status badge in header
            Surface(
                color = if (vipStatus.isVip) Color(0xFFF59E0B).copy(alpha = 0.15f) else FrostedContainer,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    1.dp,
                    if (vipStatus.isVip) Color(0xFFF59E0B) else FrostedBorder
                ),
                modifier = Modifier.clickable { onOpenVip() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (vipStatus.isVip) Icons.Default.WorkspacePremium else Icons.Default.Star,
                        contentDescription = "VIP",
                        tint = if (vipStatus.isVip) Color(0xFFF59E0B) else FrostedPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (vipStatus.isVip) "حساب طلایی VIP" else "ارتقا به VIP",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (vipStatus.isVip) Color(0xFFF59E0B) else FrostedPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun StoreVipPromoBanner(onUpgradeClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .clickable { onUpgradeClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1B4B)
        ),
        border = BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.6f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF2E1065),
                            Color(0xFF1E1B4B),
                            Color(0xFF0F172A)
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            color = Color(0xFFF59E0B),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "VIP عضویت ویژه",
                                color = Color.Black,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "دسترسی نامحدود به تمامی پک‌های لوکس و متحرک",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "بیش از ۳۰ پوسته متحرک نئونی، سه بعدی و طلایی 4K",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 11.5.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = onUpgradeClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF59E0B),
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text("مشاهده پلن‌ها", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

@Composable
private fun StoreSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("store_search_input"),
        placeholder = {
            Text(
                "جستجو در پک‌ها (طلا، نئون، طبیعت، کهکشان...)",
                fontSize = 13.sp,
                color = FrostedTextMuted
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "جستجو",
                tint = FrostedPrimary
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = FrostedGlassSolid,
            unfocusedContainerColor = FrostedGlassSolid,
            focusedBorderColor = FrostedPrimary,
            unfocusedBorderColor = FrostedBorder,
            focusedTextColor = FrostedTextPrimary,
            unfocusedTextColor = FrostedTextPrimary
        )
    )
}

@Composable
private fun StoreCategoryRow(
    categories: List<String>,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) FrostedPrimary else FrostedGlassSolid,
                border = BorderStroke(1.dp, if (isSelected) FrostedPrimary else FrostedBorder),
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onSelectCategory(category) }
            ) {
                Text(
                    text = category,
                    color = if (isSelected) Color.White else FrostedTextSecondary,
                    fontSize = 12.5.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ThemePackageCard(
    themePackage: ThemePackage,
    isUserVip: Boolean,
    downloadProgress: Float?,
    onCardClick: () -> Unit,
    onPreviewClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onVipClick: () -> Unit
) {
    val isLocked = themePackage.isVip && !isUserVip
    val isDownloading = downloadProgress != null
    val isDownloaded = themePackage.downloadStatus == "DOWNLOADED"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(22.dp))
            .clickable { onCardClick() }
            .testTag("package_card_${themePackage.id}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
        border = BorderStroke(
            1.2.dp,
            if (themePackage.isVip) Color(0xFFF59E0B).copy(alpha = 0.5f) else FrostedBorder
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Image Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        Brush.linearGradient(
                            when {
                                themePackage.category.contains("نئون") -> listOf(Color(0xFF0F172A), Color(0xFF1E1B4B), Color(0xFF06B6D4))
                                themePackage.category.contains("طبیعت") -> listOf(Color(0xFF064E3B), Color(0xFF047857), Color(0xFF10B981))
                                themePackage.category.contains("عاشقانه") -> listOf(Color(0xFF831843), Color(0xFF9D174D), Color(0xFFEC4899))
                                themePackage.category.contains("فضایی") -> listOf(Color(0xFF1E1B4B), Color(0xFF312E81), Color(0xFF6366F1))
                                else -> listOf(Color(0xFF18181B), Color(0xFF27272A), Color(0xFFD97706))
                            }
                        )
                    )
            ) {
                // Async image if present in assets
                if (themePackage.coverImage.isNotBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(AssetImage(themePackage.coverImage))
                            .crossfade(true)
                            .build(),
                        contentDescription = themePackage.titleFa,
                        modifier = Modifier.fillMaxSize(),
                        alpha = 0.65f
                    )
                }

                // Dark gradient overlay for typography
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.75f)
                                )
                            )
                        )
                )

                // Top badges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category Chip
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = themePackage.category,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // VIP or Free Badge
                    if (themePackage.isVip) {
                        Surface(
                            color = Color(0xFFF59E0B),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = "VIP",
                                    tint = Color.Black,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "پک VIP",
                                    color = Color.Black,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    } else {
                        Surface(
                            color = Color(0xFF10B981),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "رایگان",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                // Bottom package count label on cover
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Collections,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "${themePackage.themesCount} پوسته باکیفیت 4K",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Body info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = themePackage.titleFa,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = FrostedTextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = themePackage.description,
                    fontSize = 12.5.sp,
                    color = FrostedTextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                // Downloading progress bar
                if (isDownloading) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "در حال دریافت پک...",
                                fontSize = 11.sp,
                                color = FrostedPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${((downloadProgress ?: 0f) * 100).toInt()}%",
                                fontSize = 11.sp,
                                color = FrostedPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { downloadProgress ?: 0f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape),
                            color = FrostedPrimary,
                            trackColor = FrostedTintDeep
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Preview Live Button
                    OutlinedButton(
                        onClick = onPreviewClick,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, FrostedPrimary.copy(alpha = 0.6f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = FrostedPrimary
                        ),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "پیش‌نمایش",
                                modifier = Modifier.size(15.dp)
                            )
                            Text("پیش‌نمایش", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Main Action Button (Download / Locked / Detail)
                    if (isLocked) {
                        Button(
                            onClick = onVipClick,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1.3f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF59E0B),
                                contentColor = Color.Black
                            ),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "قفل",
                                    modifier = Modifier.size(14.dp)
                                )
                                Text("بازگشایی VIP", fontSize = 12.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    } else if (isDownloaded) {
                        Button(
                            onClick = onCardClick,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1.3f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF10B981),
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "دانلود شده",
                                    modifier = Modifier.size(15.dp)
                                )
                                Text("مشاهده تم‌ها", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        Button(
                            onClick = onDownloadClick,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1.3f),
                            enabled = !isDownloading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FrostedPrimary,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "دانلود",
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = if (isDownloading) "در حال دانلود..." else "دریافت پک",
                                    fontSize = 12.sp,
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

@Composable
private fun EmptyStoreState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Storefront,
            contentDescription = null,
            tint = FrostedTextMuted,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "موردی با این مشخصات یافت نشد.",
            color = FrostedTextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}