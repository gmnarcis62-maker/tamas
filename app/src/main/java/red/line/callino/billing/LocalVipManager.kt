package red.line.callino.billing

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import red.line.callino.data.CallinoRepository
import red.line.callino.data.VipStatus

/**
 * LocalVipManager - Implementation of VipManager using CallinoRepository & SharedPreferences.
 * Supports mocking / testing, offline promo codes, and can easily be connected to
 * Cafe Bazaar In-App Billing (IabHelper/Poolakey) or Myket SDK in production.
 */
class LocalVipManager(
    private val repository: CallinoRepository
) : VipManager {

    private val _isVipFlow = MutableStateFlow(repository.isVip())
    override val isVipFlow: StateFlow<Boolean> = _isVipFlow.asStateFlow()

    override fun isVip(): Boolean = repository.isVip()

    override suspend fun purchaseVip(tier: VipTier): Result<Boolean> {
        return try {
            val now = System.currentTimeMillis()
            val expiry = if (tier.durationDays > 0) {
                now + (tier.durationDays.toLong() * 24L * 60L * 60L * 1000L)
            } else {
                -1L // Lifetime
            }

            val status = VipStatus(
                isVip = true,
                purchaseDate = now,
                expiryDate = expiry,
                vipType = tier.name
            )

            repository.updateVipStatus(status)
            _isVipFlow.value = true
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun restorePurchases(): Result<Boolean> {
        val currentStatus = repository.getVipStatus()
        _isVipFlow.value = repository.isVip()
        return Result.success(repository.isVip())
    }

    override suspend fun activatePromoCode(code: String): Result<Boolean> {
        val cleanCode = code.trim().uppercase()
        // Special VIP Promo Codes for Testing / Launch campaigns: CALLINO_VIP, REDLINE2026, BAZAAR_GOLD
        return if (cleanCode == "CALLINO_VIP" || cleanCode == "REDLINE2026" || cleanCode == "BAZAAR_GOLD" || cleanCode == "VIP") {
            val now = System.currentTimeMillis()
            val expiry = now + (365L * 24L * 60L * 60L * 1000L) // 1 Year VIP
            val status = VipStatus(
                isVip = true,
                purchaseDate = now,
                expiryDate = expiry,
                vipType = "PROMO"
            )
            repository.updateVipStatus(status)
            _isVipFlow.value = true
            Result.success(true)
        } else {
            Result.failure(IllegalArgumentException("کد هدیه وارد شده معتبر نمی‌باشد."))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LocalVipManager? = null

        fun getInstance(context: Context): LocalVipManager {
            return INSTANCE ?: synchronized(this) {
                val repo = CallinoRepository.getInstance(context)
                val instance = LocalVipManager(repo)
                INSTANCE = instance
                instance
            }
        }
    }
}
