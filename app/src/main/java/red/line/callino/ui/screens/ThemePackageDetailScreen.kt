package red.line.callino.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import red.line.callino.AssetImage
import red.line.callino.data.CallTheme
import red.line.callino.data.ThemePackage
import red.line.callino.data.ThemeType
import red.line.callino.data.VipStatus
import red.line.callino.ui.theme.FrostedBg
import red.line.callino.ui.theme.FrostedBorder
import red.line.callino.ui.theme.FrostedContainer
import red.line.callino.ui.theme.FrostedGlassSolid
import red.line.callino.ui.theme.FrostedPrimary
import red.line.callino.ui.theme.FrostedSecondary
import red.line.callino.ui.theme.FrostedTextMuted
import red.line.callino.ui.theme.FrostedTextPrimary
import red.line.callino.ui.theme.FrostedTextSecondary
import red.line.callino.ui.theme.FrostedTintDeep

@Composable
fun ThemePackageDetailScreen(
    themePackage: ThemePackage,
    packageThemes: List<CallTheme>,
    activeThemeId: String,
    vipStatus: VipStatus,
    downloadProgress: Float?,
    onApplyTheme: (CallTheme) -> Unit,
    onPreviewTheme: (CallTheme) -> Unit,
    onDownloadPackage: (ThemePackage) -> Unit,
    onNavigateToVip: () -> Unit,
    onBack: () -> Unit
) {
    val isLocked = themePackage.isVip && !vipStatus.isVip
    val isDownloading = downloadProgress != null
    val isDownloaded = themePackage.downloadStatus == "DOWNLOADED"

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FrostedBg)
                .testTag("package_detail_screen")
        ) {
            // Top Bar
            Surface(
                color = FrostedGlassSolid,
                border = BorderStroke(1.dp, FrostedBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("package_detail_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "بازگشت",
                                tint = FrostedTextPrimary
                            )
                        }

                        Text(
                            text = "مشخصات و محتویات پک",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = FrostedTextPrimary
                        )
                    }

                    // VIP indicator
                    if (themePackage.isVip) {
                        Surface(
                            color = Color(0xFFF59E0B),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = "VIP",
                                    tint = Color.Black,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text("پک VIP", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 20.dp)
            ) {
                // Hero Cover Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(22.dp)),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
                        border = BorderStroke(
                            1.dp,
                            if (themePackage.isVip) Color(0xFFF59E0B).copy(alpha = 0.5f) else FrostedBorder
                        )
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
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
                                if (themePackage.coverImage.isNotBlank()) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(AssetImage(themePackage.coverImage))
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = themePackage.titleFa,
                                        modifier = Modifier.fillMaxSize(),
                                        alpha = 0.7f
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                            )
                                        )
                                )

                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(16.dp)
                                ) {
                                    Surface(
                                        color = Color.Black.copy(alpha = 0.6f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = themePackage.category,
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = themePackage.titleFa,
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }

                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = themePackage.description,
                                    fontSize = 13.sp,
                                    color = FrostedTextSecondary,
                                    lineHeight = 20.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Specifications Badges
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = FrostedContainer,
                                        border = BorderStroke(1.dp, FrostedBorder),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("تعداد تم‌ها", fontSize = 11.sp, color = FrostedTextMuted)
                                            Text("${themePackage.themesCount} پوسته", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FrostedTextPrimary)
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = FrostedContainer,
                                        border = BorderStroke(1.dp, FrostedBorder),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("کیفیت رندر", fontSize = 11.sp, color = FrostedTextMuted)
                                            Text("4K HDR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FrostedTextPrimary)
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = FrostedContainer,
                                        border = BorderStroke(1.dp, FrostedBorder),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("دسترسی", fontSize = 11.sp, color = FrostedTextMuted)
                                            Text(
                                                if (themePackage.isVip) "اشتراک VIP" else "رایگان",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (themePackage.isVip) Color(0xFFF59E0B) else Color(0xFF10B981)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Downloading progress indicator
                if (isDownloading) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = FrostedGlassSolid,
                            border = BorderStroke(1.dp, FrostedBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("در حال دریافت فایل‌های تم...", fontSize = 12.sp, color = FrostedPrimary, fontWeight = FontWeight.Bold)
                                    Text("${((downloadProgress ?: 0f) * 100).toInt()}%", fontSize = 12.sp, color = FrostedPrimary, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
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
                    }
                }

                // Themes Header in this package
                item {
                    Text(
                        text = "پوسته‌های موجود در این پک (${packageThemes.size})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = FrostedTextPrimary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Themes List in package
                items(packageThemes) { theme ->
                    val isActive = theme.id == activeThemeId

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = FrostedGlassSolid,
                        border = BorderStroke(
                            1.dp,
                            if (isActive) FrostedPrimary else FrostedBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                // Theme type icon box
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(FrostedTintDeep),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when (theme.type) {
                                            ThemeType.VIDEO -> Icons.Default.Movie
                                            ThemeType.ANIMATION -> Icons.Default.PlayCircleOutline
                                            else -> Icons.Default.Photo
                                        },
                                        contentDescription = theme.type.name,
                                        tint = FrostedPrimary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = theme.titleFa,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = FrostedTextPrimary
                                    )
                                    Text(
                                        text = when (theme.type) {
                                            ThemeType.VIDEO -> "ویدیوی متحرک 🎬"
                                            ThemeType.ANIMATION -> "انیمیشن زنده ✨"
                                            else -> "تصویر باکیفیت 🖼️"
                                        },
                                        fontSize = 11.5.sp,
                                        color = FrostedTextSecondary
                                    )
                                }
                            }

                            // Theme actions: Preview and Apply
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = { onPreviewTheme(theme) },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, FrostedPrimary.copy(alpha = 0.5f)),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = "پیش‌نمایش",
                                        tint = FrostedPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }

                                if (isActive) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF10B981).copy(alpha = 0.15f),
                                        border = BorderStroke(1.dp, Color(0xFF10B981))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "فعال",
                                                tint = Color(0xFF10B981),
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Text("فعال", fontSize = 11.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
                                        }
                                    }
                                } else {
                                    Button(
                                        onClick = { onApplyTheme(theme) },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = FrostedPrimary,
                                            contentColor = Color.White
                                        ),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text("انتخاب", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Sticky Action Bar
            Surface(
                color = FrostedGlassSolid,
                border = BorderStroke(1.dp, FrostedBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (isLocked) {
                        Button(
                            onClick = onNavigateToVip,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF59E0B),
                                contentColor = Color.Black
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = "VIP",
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "بازگشایی کل پک با اشتراک ویژه VIP",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    } else if (isDownloaded) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF10B981).copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Color(0xFF10B981)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "این پک قبلاً دریافت شده و تم‌های آن آماده استفاده است.",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF10B981)
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = { onDownloadPackage(themePackage) },
                            enabled = !isDownloading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FrostedPrimary,
                                contentColor = Color.White
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "دانلود",
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = if (isDownloading) "در حال دانلود..." else "دریافت رایگان تمام تم‌های پک",
                                    fontSize = 14.sp,
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