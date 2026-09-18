package red.line.callino.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import red.line.callino.data.AppSettings
import red.line.callino.ui.theme.FrostedBorder
import red.line.callino.ui.theme.FrostedContainer
import red.line.callino.ui.theme.FrostedGlassSolid
import red.line.callino.ui.theme.FrostedPrimary
import red.line.callino.ui.theme.FrostedSecondary
import red.line.callino.ui.theme.FrostedTextMuted
import red.line.callino.ui.theme.FrostedTextPrimary
import red.line.callino.ui.theme.FrostedTextSecondary
import red.line.callino.ui.theme.FrostedTint
import red.line.callino.ui.theme.FrostedTintDeep
import red.line.callino.ui.theme.FrostedTintLight

@Composable
fun AboutScreen(
    settings: AppSettings,
    onOpenVip: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showPrivacyDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Logo & Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val logoResId = context.resources.getIdentifier("img_callino_icon", "drawable", context.packageName)
                if (logoResId != 0) {
                    Image(
                        painter = painterResource(id = logoResId),
                        contentDescription = "آیکون تماسینو",
                        modifier = Modifier
                            .size(92.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .border(1.dp, FrostedBorder, RoundedCornerShape(24.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(FrostedPrimary, FrostedSecondary)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "درباره تماسینو",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = FrostedTextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    color = FrostedTint,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder)
                ) {
                    Text(
                        text = "نسخه ۱.۰.۰ • انتشار رسمی کافه‌بازار",
                        color = FrostedPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // VIP Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onOpenVip() },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1435)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFFD700).copy(alpha = 0.6f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFD700).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "تماسینو پلاس (VIP)",
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "بازگشایی ویدیوها، افکت‌های نئونی و تم‌های نامحدود",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFD700)
                    ) {
                        Text(
                            text = "مشاهده",
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Main About Text
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
                border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "تماسینو | Callino توسط تیم نرم‌افزاری ردلاین سافت البرز با هدف ساخت تجربه‌ای متفاوت، زیبا و شخصی‌سازی‌شده برای تماس‌های ورودی طراحی و توسعه داده شده است.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = FrostedTextPrimary,
                        lineHeight = 26.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "ما باور داریم حتی یک تماس ساده می‌تواند تجربه‌ای جذاب‌تر و شخصی‌تر داشته باشد. تماسینو تلاش می‌کند با ترکیب طراحی مدرن، امکانات شخصی‌سازی و فناوری‌های روز اندروید، ظاهر تماس‌های ورودی را از یک صفحه ساده و تکراری به تجربه‌ای متفاوت تبدیل کند.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = FrostedTextSecondary,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "هدف ما ساخت نرم‌افزارهایی کاربردی، زیبا و قابل اعتماد برای کاربران ایرانی است؛ محصولاتی که در کنار ظاهر حرفه‌ای، استفاده‌ای ساده و روان داشته باشند.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = FrostedTextSecondary,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Slogan Banner
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = FrostedTintLight,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder)
                    ) {
                        Text(
                            text = "تماسینو | هر تماس، یک تجربه متفاوت.",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = FrostedPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
                        )
                    }
                }
            }
        }

        // Developer & Management Details Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
                border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    InfoDetailRow(
                        title = "توسعه‌دهنده",
                        value = "تیم نرم‌افزاری ردلاین سافت البرز",
                        icon = Icons.Default.Business,
                        accentColor = FrostedPrimary
                    )

                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(FrostedBorder))

                    InfoDetailRow(
                        title = "مدیریت",
                        value = "مهندس مهدی رضایی",
                        icon = Icons.Default.Person,
                        accentColor = FrostedSecondary
                    )

                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(FrostedBorder))

                    InfoDetailRow(
                        title = "پشتیبانی",
                        value = settings.supportEmail,
                        icon = Icons.Default.Email,
                        accentColor = FrostedPrimary
                    )
                }
            }
        }

        // Privacy Policy Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { showPrivacyDialog = true },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
                border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(FrostedTintLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Policy,
                                contentDescription = null,
                                tint = FrostedPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "حریم خصوصی و شفافیت داده‌ها",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FrostedTextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "اطلاعات تماس و مخاطبین کاملاً آفلاین پردازش می‌شوند.",
                                style = MaterialTheme.typography.bodySmall,
                                color = FrostedTextSecondary
                            )
                        }
                    }

                    Text(
                        text = "مطالعه ←",
                        color = FrostedPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Contact Support Email Action Button
        item {
            Button(
                onClick = {
                    sendEmailIntent(context, settings.supportEmail)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("contact_support_btn"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FrostedPrimary,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "✉ ارتباط با پشتیبانی",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Cafe Bazaar Review & Rating Button (Centralized link configuration)
        item {
            Button(
                onClick = {
                    openCafeBazaarIntent(context, settings.cafeBazaarPackageUrl, settings.cafeBazaarWebUrl)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("bazaar_review_btn"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FrostedTint,
                    contentColor = FrostedPrimary
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "⭐ ثبت نظر در کافه‌بازار",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = FrostedPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "سیاست حفظ حریم خصوصی",
                        fontWeight = FontWeight.Bold,
                        color = FrostedTextPrimary
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "برنامه تماسینو با احترام کامل به حریم خصوصی کاربران گرامی:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = FrostedTextPrimary
                    )
                    Text(
                        text = "۱. هیچ‌گونه اطلاعات شخصی، شماره تلفن، فهرست مخاطبین یا سوابق تماس به سرور خارجی ارسال نمی‌شود.",
                        fontSize = 12.sp,
                        color = FrostedTextSecondary,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = "۲. تمامی رسانه‌ها، ویدیوها و تصاویر بارگذاری‌شده صرفاً در فضای حافظه داخلی و محافظت‌شده اپلیکیشن نگهداری می‌شوند.",
                        fontSize = 12.sp,
                        color = FrostedTextSecondary,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = "۳. دسترسی‌های درخواستی اندروید صرفاً جهت نمایش نام مخاطب و فعال‌سازی رابط تمام‌صفحه تماس ورودی استفاده می‌گردد.",
                        fontSize = 12.sp,
                        color = FrostedTextSecondary,
                        lineHeight = 20.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPrivacyDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = FrostedPrimary)
                ) {
                    Text("متوجه شدم")
                }
            },
            containerColor = FrostedGlassSolid,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun InfoDetailRow(
    title: String,
    value: String,
    icon: ImageVector,
    accentColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(FrostedTintLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = FrostedTextSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = FrostedTextPrimary
            )
        }
    }
}

private fun sendEmailIntent(context: Context, email: String) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, "پشتیبانی برنامه تماسینو")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, "ارسال ایمیل به پشتیبانی"))
    } catch (e: Exception) {
        // Handle error gracefully
    }
}

private fun openCafeBazaarIntent(context: Context, bazaarUri: String, fallbackUrl: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(bazaarUri)
            setPackage("com.farsitel.bazaar")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback to web browser
        try {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(webIntent)
        } catch (ignored: Exception) {}
    }
}
