package red.line.callino.ui.effects.premium

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import red.line.callino.data.EffectType

@Composable
fun PremiumEffectRenderer(
    effect: EffectType,
    modifier: Modifier = Modifier,
    animationsEnabled: Boolean = true,
    intensity: Float = 1f
) {
    if (!animationsEnabled || effect == EffectType.NONE) return

    Box(modifier = modifier.fillMaxSize()) {
        when (effect) {
            EffectType.MESH_GRADIENT      -> MeshGradientEffect(intensity)
            EffectType.INK_FLOW           -> InkFlowEffect(intensity)
            EffectType.NEBULA             -> NebulaEffect(intensity)
            EffectType.PREMIUM_AURORA     -> PremiumAuroraEffect(intensity)

            EffectType.LIQUID_GLASS       -> LiquidGlassEffect(intensity)
            EffectType.GLASS_REFRACTION   -> GlassRefractionEffect(intensity)
            EffectType.GLASS_MORPH        -> GlassMorphEffect(intensity)

            EffectType.VOLUMETRIC_BEAMS   -> VolumetricBeamsEffect(intensity)
            EffectType.BLOOM              -> BloomEffect(intensity)
            EffectType.PHOTON_BEAMS       -> PhotonBeamsEffect(intensity)
            EffectType.PRISM_LIGHT        -> PrismLightEffect(intensity)
            EffectType.NEON_PULSE         -> NeonPulseEffect(intensity)
            EffectType.ENERGY_AURA        -> EnergyAuraEffect(intensity)

            EffectType.HOLOGRAPHIC_FOIL   -> HolographicFoilEffect(intensity)
            EffectType.CHROME_METAL       -> ChromeMetalEffect(intensity)
            EffectType.LIQUID_METAL       -> LiquidMetalEffect(intensity)
            EffectType.CRYSTAL_PRISM      -> CrystalPrismEffect(intensity)

            EffectType.CONSTELLATION      -> ConstellationEffect(intensity)
            EffectType.COSMIC_DUST        -> CosmicDustEffect(intensity)
            EffectType.CAUSTICS           -> CausticsEffect(intensity)
            EffectType.CHROMATIC_ABERRATION -> ChromaticAberrationEffect(intensity)

            EffectType.GLITCH_NEON        -> GlitchNeonEffect(intensity)
            EffectType.SONIC_WAVE         -> SonicWaveEffect(intensity)
            EffectType.DEPTH_PARALLAX     -> DepthParallaxEffect(intensity)

            EffectType.NONE               -> Unit
        }

        GrainOverlay(baseAlpha = 0.05f)
    }
}