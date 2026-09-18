package red.line.callino.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

data class GuideItem(
    val id: Int,
    val title: String,
    val summary: String,
    val description: String,
    val icon: ImageVector,
    val accentColor: Color
)

@Composable
fun GuideScreen(
    onOpenCallSimulator: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var expandedIndex by remember { mutableIntStateOf(0) }
    var showPermissionsDialog by remember { mutableStateOf(false) }

    val guideItems = remember {
        listOf(
            GuideItem(
                id = 1,
                title = "۱. شروع کار",
                summary = "مراحل اولیه فعال‌سازی و راه‌اندازی تماسینو",
                description = "برای استفاده از تماسینو ابتدا برنامه را باز کنید و دسترسی‌های موردنیاز را فعال کنید.\nپس از فعال‌سازی دسترسی‌ها، می‌توانید ظاهر دلخواه خود را برای تماس‌های ورودی انتخاب کنید و لذت ببرید.",
                icon = Icons.Default.PlayArrow,
                accentColor = FrostedPrimary
            ),
            GuideItem(
                id = 2,
                title = "۲. انتخاب ظاهر تماس",
                summary = "امکانات انتخاب تصویر، ویدیو، انیمیشن و تم شخصی",
                description = "از بخش «ظاهر تماس» می‌توانید صفحه تماس ورودی را کاملاً شخصی‌سازی کنید.\nامکان انتخاب انواع محتوا شامل: تصاویر باکیفیت، ویدیوهای متحرک، انیمیشن‌های نوری زنده و محتوای شخصی از حافظه گوشی فراهم است.",
                icon = Icons.Default.Palette,
                accentColor = FrostedSecondary
            ),
            GuideItem(
                id = 3,
                title = "۳. استفاده از تصاویر آماده",
                summary = "مشاهده، پیش‌نمایش و انتخاب تصاویر پس‌زمینه",
                description = "تماسینو مجموعه‌ای از تصاویر آماده باکیفیت بالا و الگوهای اصیل دارد.\nکافی است وارد بخش «ظاهر تماس ← تصاویر» شوید و تصویر دلخواه خود را مشاهده، پیش‌نمایش و فعال کنید.",
                icon = Icons.Default.Image,
                accentColor = FrostedPrimary
            ),
            GuideItem(
                id = 4,
                title = "۴. استفاده از ویدئو",
                summary = "تنظیم ویدیوی متحرک برای صفحه تماس",
                description = "در بخش ویدئوها، می‌توانید ویدیوهای جذاب متحرک را برای صفحه تماس انتخاب کنید یا با افزودن فایل ویدئویی شخصی از حافظه گوشی، تماس ورودی را ویدیویی نمایید.",
                icon = Icons.Default.Videocam,
                accentColor = Color(0xFF0284C7)
            ),
            GuideItem(
                id = 5,
                title = "۵. استفاده از انیمیشن",
                summary = "انیمیشن‌های نوری، نئون، امواج و ذرات معلق",
                description = "تماسینو دارای انیمیشن‌های سبک و پویایی چون امواج اقیانوسی، شفق نوری، ذرات نورانی و نبض سایبرپانک است که بدون مصرف اضافه باتری، زیبایی صفحه تماس را دوچندان می‌کند.",
                icon = Icons.Default.Animation,
                accentColor = Color(0xFF7C3AED)
            ),
            GuideItem(
                id = 6,
                title = "۶. افزودن محتوای شخصی",
                summary = "مسیر: محتوای من ← افزودن محتوا",
                description = "شما می‌توانید هر زمان محتوای شخصی (عکس یا فیلم) را از حافظه گوشی اضافه کنید.\nمسیر پیشنهادی: تنظیمات ← محتوای من ← افزودن محتوا.\nدر این بخش امکان مشاهده، انتخاب، تغییر نام، حذف و استفاده به عنوان صفحه تماس وجود دارد.",
                icon = Icons.Default.VideoLibrary,
                accentColor = Color(0xFFE11D48)
            ),
            GuideItem(
                id = 7,
                title = "۷. اختصاص ظاهر به مخاطب خاص",
                summary = "تنظیم تم‌های متفاوت برای مادر، دوست، همکار...",
                description = "یکی از قابلیت‌های کلیدی تماسینو:\nمی‌توانید برای مخاطب «مادر» یک تصویر پرمهر، برای مخاطب «دوست» یک تم نئون و برای مخاطب «همکار» تم مینیمال قرار دهید. در صورت عدم تعیین، از تم پیش‌فرض استفاده می‌شود.",
                icon = Icons.Default.Contacts,
                accentColor = Color(0xFF059669)
            ),
            GuideItem(
                id = 8,
                title = "۸. پیش‌نمایش تماس",
                summary = "دکمه 👁 پیش‌نمایش تماس برای شبیه‌سازی واقعی",
                description = "قبل از هر انتخابی، می‌توانید با فشردن دکمه «پیش‌نمایش تماس» ظاهر صفحه را دقیقاً مانند یک تماس واقعی با دکمه‌های پاسخ، رد، انیمیشن و مشخصات مخاطب مشاهده و آزمایش کنید.",
                icon = Icons.Default.Visibility,
                accentColor = FrostedPrimary
            ),
            GuideItem(
                id = 9,
                title = "۹. شخصی‌سازی صفحه تماس",
                summary = "تنظیم نام، شماره، اندازه، موقعیت، Blur، دکمه‌ها",
                description = "در بخش تنظیمات می‌توانید تمام جزئیات را شخصی‌سازی کنید:\n• نمایش یا عدم نمایش نام و شماره\n• اندازه قلم و نوشته‌ها\n• موقعیت اطلاعات (بالا، مرکز، پایین)\n• نوع دکمه‌ها (مدرن، کلاسیک، سوایپ)\n• میزان تاریکی و بلور پس‌زمینه\n• فعال/غیرفعال‌سازی انیمیشن‌ها",
                icon = Icons.Default.Tune,
                accentColor = Color(0xFFD97706)
            ),
            GuideItem(
                id = 10,
                title = "۱۰. دسترسی‌های برنامه",
                summary = "🔐 توضیحات ساده برای هر دسترسی موردنیاز",
                description = "تماسینو تنها دسترسی‌های ضروری را جهت نمایش صفحه تماس درخواست می‌کند:\n• تماس و وضعیت تلفن (برای تشخیص لحظه تماس ورودی)\n• مخاطبین (برای نمایش نام و تم اختصاصی هر مخاطب)\n• نمایش روی سایر برنامه‌ها (برای ظاهر شدن روی صفحه هنگام زنگ خوردن)",
                icon = Icons.Default.Lock,
                accentColor = FrostedPrimary
            ),
            GuideItem(
                id = 11,
                title = "۱۱. اگر صفحه تماس نمایش داده نشد",
                summary = "۵ راهکار سریع برای حل مشکل نمایش تماس",
                description = "اگر صفحه تماس نمایش داده نشد:\n۱. دسترسی‌های موردنیاز فعال باشند.\n۲. برنامه توسط سیستم محدود نشده باشد.\n۳. محدودیت بهینه‌سازی باتری غیرفعال باشد.\n۴. اجازه «نمایش روی سایر برنامه‌ها» فعال باشد.\n۵. دسترسی اعلان‌ها تایید شده باشد.",
                icon = Icons.Default.Warning,
                accentColor = Color(0xFFEF4444)
            ),
            GuideItem(
                id = 12,
                title = "۱۲. بازگردانی تنظیمات",
                summary = "بازنشانی ایمن با تأیید «آیا مطمئن هستید؟»",
                description = "در بخش تنظیمات گزینه «بازگردانی تنظیمات» قرار دارد که با نمایش کادر تأیید «آیا مطمئن هستید؟» تمام موارد را به مقادیر استاندارد بازمی‌گرداند.",
                icon = Icons.Default.Refresh,
                accentColor = FrostedSecondary
            )
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "📖 راهنمای تماسینو",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = FrostedTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "آموزش کامل کار با قابلیت‌ها و حل مشکلات احتمالی",
                    style = MaterialTheme.typography.bodySmall,
                    color = FrostedTextSecondary
                )
            }
        }

        // Quick action banner: Check permissions
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
                border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(FrostedTintLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = FrostedPrimary)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "بررسی وضعیت دسترسی‌ها",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FrostedTextPrimary
                            )
                            Text(
                                text = "اطمینان از عملکرد روان و بدون وقفه تماسینو",
                                style = MaterialTheme.typography.bodySmall,
                                color = FrostedTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { showPermissionsDialog = true },
                            modifier = Modifier.weight(1f).height(46.dp).testTag("check_permissions_btn"),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FrostedPrimary, contentColor = Color.White),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Text("بررسی وضعیت دسترسی‌ها", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Button(
                            onClick = onOpenCallSimulator,
                            modifier = Modifier.weight(1f).height(46.dp).testTag("guide_preview_call_btn"),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FrostedTint, contentColor = FrostedPrimary),
                            border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
                        ) {
                            Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("پیش‌نمایش تماس", fontWeight = FontWeight.Medium, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Guide Accordion Cards (1 to 12)
        itemsIndexed(guideItems) { index, item ->
            val isExpanded = expandedIndex == index

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { expandedIndex = if (isExpanded) -1 else index }
                    .testTag("guide_card_${item.id}"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FrostedGlassSolid),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isExpanded) FrostedPrimary else FrostedBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = item.accentColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FrostedTextPrimary
                            )
                            if (!isExpanded) {
                                Text(
                                    text = item.summary,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FrostedTextSecondary,
                                    maxLines = 1
                                )
                            }
                        }

                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "بستن" else "باز کردن",
                            tint = FrostedTextSecondary
                        )
                    }

                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(modifier = Modifier.padding(top = 12.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(FrostedBorder)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = FrostedTextPrimary,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Permissions Checker Dialog
    if (showPermissionsDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionsDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = FrostedPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("🔐 دسترسی‌های موردنیاز تماسینو", color = FrostedTextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    item {
                        PermissionExplainItem(
                            title = "وضعیت تماس (READ_PHONE_STATE)",
                            why = "این دسترسی برای چیست؟",
                            reason = "برای این‌که برنامه متوجه زنگ خوردن گوشی شود و صفحه زیبای تماسینو را بالا بیاورد.",
                            how = "از منوی تنظیمات گوشی > برنامه‌ها > تماسینو > دسترسی‌ها فعال شود."
                        )
                    }
                    item {
                        PermissionExplainItem(
                            title = "مخاطبین (READ_CONTACTS)",
                            why = "چرا تماسینو به آن نیاز دارد؟",
                            reason = "برای شناسایی نام تماس‌گیرنده و اعمال تم اختصاصی (مثلاً تم مادر یا دوست).",
                            how = "هنگام درخواست سیستم گزینه «اجازه دادن / Allow» را انتخاب کنید."
                        )
                    }
                    item {
                        PermissionExplainItem(
                            title = "نمایش روی صفحه (SYSTEM_ALERT_WINDOW)",
                            why = "چگونه آن را فعال کنیم؟",
                            reason = "جهت نمایش تمام‌صفحه و باکیفیت در لحظه ورود تماس حتی در حالت قفل.",
                            how = "در تنظیمات گوشی گزینه «نمایش روی سایر برنامه‌ها» را فعال نمایید."
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionsDialog = false
                        try {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Fallback
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FrostedPrimary, contentColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("باز کردن تنظیمات گوشی", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionsDialog = false }) {
                    Text("متوجه شدم", color = FrostedTextSecondary)
                }
            },
            containerColor = FrostedGlassSolid,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
private fun PermissionExplainItem(
    title: String,
    why: String,
    reason: String,
    how: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FrostedTintLight),
        border = androidx.compose.foundation.BorderStroke(1.dp, FrostedBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, color = FrostedPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "$why $reason", color = FrostedTextPrimary, fontSize = 12.sp, lineHeight = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "راهنما: $how", color = FrostedTextSecondary, fontSize = 11.sp)
        }
    }
}
