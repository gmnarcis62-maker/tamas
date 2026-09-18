package red.line.callino.billing

import kotlinx.coroutines.flow.StateFlow

/**
 * VipType - Subscription or lifetime tier for Callino.
 */
enum class VipTier(val id: String, val titleFa: String, val priceFa: String, val durationDays: Int) {
    MONTHLY("callino_vip_monthly", "اشتراک ۱ ماهه VIP", "۳۹,۰۰۰ تومان", 30),
    THREE_MONTHS("callino_vip_3months", "اشتراک ۳ ماهه VIP (ویژه)", "۸۹,۰۰۰ تومان", 90),
    LIFETIME("callino_vip_lifetime", "اشتراک دائمی طلایی (مادام‌العمر)", "۱۷۹,۰۰۰ تومان", -1)
}

/**
 * VipManager - Abstraction layer ready for Cafe Bazaar / Myket In-App Billing or Backend Licensing.
 */
interface VipManager {
    val isVipFlow: StateFlow<Boolean>
    
    fun isVip(): Boolean
    
    suspend fun purchaseVip(tier: VipTier): Result<Boolean>
    
    suspend fun restorePurchases(): Result<Boolean>
    
    suspend fun activatePromoCode(code: String): Result<Boolean>
}
